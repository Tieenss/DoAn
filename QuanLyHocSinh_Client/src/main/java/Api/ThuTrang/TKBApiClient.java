package Api.ThuTrang;

import Api.ApiConfig;
import Model.TKB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class TKBApiClient {
    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/tkb";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<TKB> getAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<TKB>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public TKB getById(String maTKB) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maTKB))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) return null;
        return gson.fromJson(response.body(), TKB.class);
    }

    public List<TKB> getByFilter(String maLop, String maMH, int thu) throws Exception {
        return getByFilter(maLop, maMH, thu, "", 0);
    }

    public List<TKB> getByFilter(String maLop, String maMH, int thu, String namHoc, int hocKy) throws Exception {
        String url = BASE_URL + "/filter?maLop=" + URLEncoder.encode(maLop != null ? maLop : "", StandardCharsets.UTF_8)
                + "&maMH=" + URLEncoder.encode(maMH != null ? maMH : "", StandardCharsets.UTF_8)
                + "&thu=" + thu
                + "&namHoc=" + URLEncoder.encode(namHoc != null ? namHoc : "", StandardCharsets.UTF_8)
                + "&hocKy=" + hocKy;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Lỗi server (" + response.statusCode() + "): " + response.body());
        }
        Type type = new TypeToken<List<TKB>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public List<String> getDanhSachNamHoc() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/danhsachnamhoc"))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            return new java.util.ArrayList<>();
        }
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public List<Map<String, String>> getDanhSachGV() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/danhsachgv"))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public List<Map<String, String>> getDanhSachLopTatCa() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/danhsachlop/tatca"))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public List<Map<String, String>> getDanhSachPhong() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/danhsachphong"))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public List<Map<String, String>> getDanhSachMon() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/danhsachmon"))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public TKB create(TKB tkb) throws Exception {
        String json = gson.toJson(tkb);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 409) {
            String msg = response.body();
            if (msg == null || msg.trim().isEmpty()) {
                msg = "Trùng lịch học (lớp, GV hoặc phòng đã có lịch)!";
            }
            throw new Exception(msg);
        }
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Lỗi khi thêm: " + response.body());
        }
        return gson.fromJson(response.body(), TKB.class);
    }

    public TKB update(String maTKB, TKB tkb) throws Exception {
        String json = gson.toJson(tkb);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maTKB))
                .header("Content-Type", "application/json; charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 409) {
            String msg = response.body();
            if (msg == null || msg.trim().isEmpty()) {
                msg = "Trùng lịch học (lớp, GV hoặc phòng đã có lịch)!";
            }
            throw new Exception(msg);
        }
        if (response.statusCode() == 404) throw new Exception("Không tìm thấy TKB");
        if (response.statusCode() != 200) {
            throw new Exception("Lỗi khi cập nhật: " + response.body());
        }
        return gson.fromJson(response.body(), TKB.class);
    }

    public void delete(String maTKB) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maTKB))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) throw new Exception("Không tìm thấy TKB");
    }
}