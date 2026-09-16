package Api.ThuTrang;

import Api.ApiConfig;
import Model.MonHoc;
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

public class MonHocApiClient {
    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/monhoc";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<MonHoc> getAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<MonHoc>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public MonHoc getById(String maMH) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maMH))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) return null;
        return gson.fromJson(response.body(), MonHoc.class);
    }

    public boolean exists(String maMH) throws Exception {
        return getById(maMH) != null;
    }

    public MonHoc create(MonHoc monHoc) throws Exception {
        String json = gson.toJson(monHoc);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 409 || response.statusCode() == 422 || response.statusCode() == 400) {
            String msg = response.body();
            if (msg == null || msg.trim().isEmpty()) {
                msg = (response.statusCode() == 409) ? "Mã môn học đã tồn tại!" : "Tên môn học đã tồn tại!";
            }
            throw new Exception(msg);
        }
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Lỗi khi thêm môn học: " + response.body());
        }
        return gson.fromJson(response.body(), MonHoc.class);
    }

    public MonHoc update(String maMH, MonHoc monHoc) throws Exception {
        String json = gson.toJson(monHoc);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maMH))
                .header("Content-Type", "application/json; charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 409 || response.statusCode() == 422 || response.statusCode() == 400) {
            String msg = response.body();
            if (msg == null || msg.trim().isEmpty()) {
                msg = "Tên môn học đã tồn tại!";
            }
            throw new Exception(msg);
        }
        if (response.statusCode() == 404) throw new Exception("Không tìm thấy môn học");
        if (response.statusCode() != 200) {
            throw new Exception("Lỗi khi cập nhật môn học: " + response.body());
        }
        return gson.fromJson(response.body(), MonHoc.class);
    }

    public void delete(String maMH) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maMH))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) throw new Exception("Không tìm thấy môn học");
    }

    public List<MonHoc> search(String keyword) throws Exception {
        String url = BASE_URL + "/search?keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<MonHoc>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }
}