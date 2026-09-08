package Api.Đai;

import Api.ApiConfig;
import Model.TaiKhoan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaiKhoanApi {

    private static final String SERVER_URL = ApiConfig.BASE_URL+ "/api/taikhoan";

    private final HttpClient client;
    private final Gson gson;

    public TaiKhoanApi() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        gson = new Gson();
    }

    public String checkLogin(String user, String pass) {
        TaiKhoan tk = checkLoginFull(user, pass);
        if (tk != null) {
            return tk.getQuyen();
        }
        return null;
    }

    public TaiKhoan checkLoginFull(String tenDangNhap, String matKhau) {

        try {

            Map<String, String> data = new HashMap<>();
            data.put("tenDangNhap", tenDangNhap);
            data.put("matKhau", matKhau);

            String json = gson.toJson(data);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), TaiKhoan.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<TaiKhoan> getAll() {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                Type listType = new TypeToken<ArrayList<TaiKhoan>>(){}.getType();

                return gson.fromJson(response.body(), listType);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public boolean insert(TaiKhoan tk) {

        try {

            String json = gson.toJson(tk);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(TaiKhoan tk) {

        try {

            String json = gson.toJson(tk);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(String tenDangNhap) {

        try {

            String url = SERVER_URL + "/"
                    + URLEncoder.encode(tenDangNhap, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<TaiKhoan> search(String keyword) {

        try {

            String url = SERVER_URL + "/search?keyword="
                    + URLEncoder.encode(keyword, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                Type listType = new TypeToken<ArrayList<TaiKhoan>>(){}.getType();

                return gson.fromJson(response.body(), listType);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}