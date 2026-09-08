package Api.Tien;

import Api.ApiConfig;
import Model.LichThi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

public class LichThiApi {

    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/lichthi";
    private final HttpClient client;
    private final Gson gson;

    public LichThiApi() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        gson = new Gson();
    }

    public List<LichThi> getAllLichThi() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Type type = new TypeToken<List<LichThi>>(){}.getType();
                return gson.fromJson(response.body(), type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<LichThi> searchLichThi(String keyword) {
        try {
            String url = String.format("%s/search?keyword=%s",
                    BASE_URL,
                    URLEncoder.encode(keyword, StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Type type = new TypeToken<List<LichThi>>(){}.getType();
                return gson.fromJson(response.body(), type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<String> getDistinctKyThi() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/kythi"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Type type = new TypeToken<List<String>>(){}.getType();
                return gson.fromJson(response.body(), type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<LichThi> getLichThiByFilter(String kyThi, String maMH, String maPhong, String maLop) {
        try {
            String url = String.format("%s/filter?kyThi=%s&maMH=%s&maPhong=%s&maLop=%s",
                    BASE_URL,
                    URLEncoder.encode(kyThi, StandardCharsets.UTF_8),
                    URLEncoder.encode(maMH, StandardCharsets.UTF_8),
                    URLEncoder.encode(maPhong, StandardCharsets.UTF_8),
                    URLEncoder.encode(maLop, StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Type type = new TypeToken<List<LichThi>>(){}.getType();
                return gson.fromJson(response.body(), type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private String formatTime(String rawTime) {
        if (rawTime == null) return "";
        if (rawTime.length() >= 5) {
            return rawTime.substring(0, 5); 
        }
        return rawTime;
    }

    public boolean addLichThi(LichThi lt) {
        try {
            String json = gson.toJson(lt);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateLichThi(LichThi lt) {
        try {
            String json = gson.toJson(lt);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + lt.getMaLT()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLichThi(int maLT) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + maLT))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
