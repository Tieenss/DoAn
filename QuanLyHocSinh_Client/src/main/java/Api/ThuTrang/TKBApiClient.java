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
        String url = BASE_URL + "/filter?maLop=" + URLEncoder.encode(maLop, StandardCharsets.UTF_8)
                + "&maMH=" + URLEncoder.encode(maMH, StandardCharsets.UTF_8)
                + "&thu=" + thu;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<TKB>>(){}.getType();
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
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 409) throw new Exception("Trùng tiết học (lớp, GV hoặc phòng đã có lịch)");
        return gson.fromJson(response.body(), TKB.class);
    }

    public TKB update(String maTKB, TKB tkb) throws Exception {
        String json = gson.toJson(tkb);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maTKB))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) throw new Exception("Không tìm thấy TKB");
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