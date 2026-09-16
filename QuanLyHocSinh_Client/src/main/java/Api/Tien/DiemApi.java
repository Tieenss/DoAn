package Api.Tien;

import Api.ApiConfig;
import Model.Diem;
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

public class DiemApi {
    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/diem";
    private final HttpClient client;
    private final Gson gson;

    public DiemApi() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        gson = new Gson();
    }

    public List<Diem> getAll() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<Diem>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Diem> getDiemByFilter(String maLop, String maMH, int hocKy, String namHoc) {
        try {
            String url = String.format("%s/filter?maLop=%s&maMH=%s&hocKy=%d&namHoc=%s", 
                BASE_URL,
                URLEncoder.encode(maLop, StandardCharsets.UTF_8),
                URLEncoder.encode(maMH, StandardCharsets.UTF_8),
                hocKy,
                URLEncoder.encode(namHoc != null ? namHoc : "", StandardCharsets.UTF_8));
                
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<Diem>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean checkExists(String maHS, String maMH, int hocKy, String namHoc) {
        try {
            String url = String.format("%s/exists?maHS=%s&maMH=%s&hocKy=%d&namHoc=%s",
                    BASE_URL,
                    URLEncoder.encode(maHS, StandardCharsets.UTF_8),
                    URLEncoder.encode(maMH, StandardCharsets.UTF_8),
                    hocKy,
                    URLEncoder.encode(namHoc != null ? namHoc : "", StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Boolean.parseBoolean(response.body().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String addDiem(Diem d) {
        try {
            String jsonBody = gson.toJson(d);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return null; // Thành công
            } else if (response.statusCode() == 409) {
                return response.body(); // Thông báo trùng lặp từ Server
            } else {
                return "Lỗi máy chủ (" + response.statusCode() + "): " + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi kết nối máy chủ: " + e.getMessage();
        }
    }

    public String updateDiemResult(Diem d) {
        try {
            String jsonBody = gson.toJson(d);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return null; // Thành công
            } else {
                return "Lỗi máy chủ (" + response.statusCode() + "): " + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi kết nối máy chủ: " + e.getMessage();
        }
    }

    public boolean updateDiem(Diem d) {
        return updateDiemResult(d) == null;
    }

    public String deleteDiem(String maHS, String maMH, int hocKy, String namHoc) {
        try {
            String url = String.format("%s?maHS=%s&maMH=%s&hocKy=%d&namHoc=%s",
                    BASE_URL,
                    URLEncoder.encode(maHS, StandardCharsets.UTF_8),
                    URLEncoder.encode(maMH, StandardCharsets.UTF_8),
                    hocKy,
                    URLEncoder.encode(namHoc != null ? namHoc : "", StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return null; // Thành công
            } else {
                return "Lỗi xóa (" + response.statusCode() + "): " + response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi kết nối: " + e.getMessage();
        }
    }

    public List<Diem> searchDiem(String keyword) {
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
                return gson.fromJson(response.body(), new TypeToken<List<Diem>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Integer> getDistinctHocKy() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/hocky"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<Integer>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<String> getDistinctNamHoc() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/namhoc"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<String>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Diem> getDiemByMaHS(String maHS) {
        try {
            String url = String.format("%s/hocsinh/%s", 
                BASE_URL,
                URLEncoder.encode(maHS, StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<Diem>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
