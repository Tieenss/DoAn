package Api.HaTrang;

import Api.ApiConfig;
import Model.Hocphi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HocPhiApiClient {
    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/hocphi";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Hocphi> getAllHocPhi() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type type = new TypeToken<List<Hocphi>>() {}.getType();
            return gson.fromJson(response.body(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); 
        }
    }

    public List<Hocphi> getHocPhiByLop(String maLop, int hocKy, String namHoc) {
        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL).append("/filter?");
            urlBuilder.append("maLop=").append(URLEncoder.encode(maLop != null ? maLop : "", StandardCharsets.UTF_8));
            urlBuilder.append("&hocKy=").append(hocKy);
            urlBuilder.append("&namHoc=").append(URLEncoder.encode(namHoc != null ? namHoc : "", StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder.toString()))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type type = new TypeToken<List<Hocphi>>() {}.getType();
            return gson.fromJson(response.body(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Hocphi> getByMaHS(String maHS) {
        try {
            String url = BASE_URL + "/hocsinh/" + URLEncoder.encode(maHS, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type type = new TypeToken<List<Hocphi>>() {}.getType();
            return gson.fromJson(response.body(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean saveHocPhi(Hocphi hp) {
        try {
            String json = gson.toJson(hp);
            HttpRequest request;

            if (hp.getMaHP() <= 0) {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
            } else {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/" + hp.getMaHP()))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(json))
                        .build();
            }

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status = " + response.statusCode());
            System.out.println("Response = " + response.body());

            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHocPhi(int maHP) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + maHP))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getNamHocByMaLop(String maLop) {
        try {
            String url = BASE_URL + "/namhoc?maLop=" + URLEncoder.encode(maLop, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Type type = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(response.body(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}