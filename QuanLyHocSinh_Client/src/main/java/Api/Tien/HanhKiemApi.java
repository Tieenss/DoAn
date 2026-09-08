package Api.Tien;

import Api.ApiConfig;
import Model.HanhKiem;
import Model.Auth;
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

public class HanhKiemApi {
    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/hanhkiem";
    private final HttpClient client;
    private final Gson gson;

    public HanhKiemApi() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        gson = new Gson();
    }

    private List<HanhKiem> parseResponse(String jsonBody) {
        Type type = new TypeToken<List<HanhKiem>>(){}.getType();
        List<HanhKiem> list = gson.fromJson(jsonBody, type);
        return list != null ? list : new ArrayList<>();
    }

    public List<HanhKiem> getHanhKiemByFilter(String maLop, String namHoc, int hocKy) {
        try {
            String url = String.format("%s/filter?maLop=%s&namHoc=%s&hocKy=%d",
                    BASE_URL,
                    URLEncoder.encode(maLop, StandardCharsets.UTF_8),
                    URLEncoder.encode(namHoc, StandardCharsets.UTF_8),
                    hocKy);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) throw new Exception("Lỗi Server: " + response.body());
            return parseResponse(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<HanhKiem> searchHanhKiem(String keyword) {
        try {
            String url = String.format("%s/search?keyword=%s",
                    BASE_URL,
                    URLEncoder.encode(keyword, StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) throw new Exception("Lỗi Server: " + response.body());
            return parseResponse(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean saveHanhKiem(HanhKiem hk) {
        if (!Auth.canEditData(hk.getMaHS())) {
            System.out.println("Bạn không có quyền cập nhật hạnh kiểm cho học sinh này!");
            return false;
        }

        try {
            String json = gson.toJson(hk);
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

    public boolean deleteHanhKiem(String maHS, String namHoc, int hocKy) {
        if (!Auth.canEditData(maHS)) {
            System.out.println("Bạn không có quyền xóa hạnh kiểm cho học sinh này!");
            return false;
        }

        try {
            String url = String.format("%s?maHS=%s&namHoc=%s&hocKy=%d",
                    BASE_URL,
                    URLEncoder.encode(maHS, StandardCharsets.UTF_8),
                    URLEncoder.encode(namHoc, StandardCharsets.UTF_8),
                    hocKy);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getDistinctNamHoc() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/namhoc"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) throw new Exception("Lỗi Server: " + response.body());
            Type type = new TypeToken<List<String>>(){}.getType();
            return gson.fromJson(response.body(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<HanhKiem> getHanhKiemByMaHS(String maHS) {
        try {
            String url = String.format("%s/mahs/%s",
                    BASE_URL,
                    URLEncoder.encode(maHS, StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) throw new Exception("Lỗi Server: " + response.body());
            return parseResponse(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<HanhKiem> getHanhKiemByMaHSWithPermission(String maHS) {
        if (!Auth.canViewHocSinh(maHS)) {
            return new ArrayList<>();
        }
        return getHanhKiemByMaHS(maHS);
    }

    public List<HanhKiem> searchHanhKiemByMaHS(String maHS, String keyword) {
        
        List<HanhKiem> list = getHanhKiemByMaHS(maHS);
        List<HanhKiem> filtered = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (HanhKiem hk : list) {
            if (hk.getMaHS().toLowerCase().contains(lowerKeyword) || 
                (hk.getTenHS() != null && hk.getTenHS().toLowerCase().contains(lowerKeyword))) {
                filtered.add(hk);
            }
        }
        return filtered;
    }
}
