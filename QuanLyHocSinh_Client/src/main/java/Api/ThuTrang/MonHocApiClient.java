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
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 409) throw new Exception("Mã môn đã tồn tại");
        if (response.statusCode() == 422) throw new Exception("Tên môn đã tồn tại");
        return gson.fromJson(response.body(), MonHoc.class);
    }

    public MonHoc update(String maMH, MonHoc monHoc) throws Exception {
        String json = gson.toJson(monHoc);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maMH))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) throw new Exception("Không tìm thấy môn học");
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