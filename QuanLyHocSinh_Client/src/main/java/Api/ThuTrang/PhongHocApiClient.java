package Api.ThuTrang;

import Api.ApiConfig;
import Model.PhongHoc;
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

public class PhongHocApiClient {
    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/phonghoc";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<PhongHoc> getAll() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<PhongHoc>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }

    public PhongHoc getById(String maPH) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maPH))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) return null;
        return gson.fromJson(response.body(), PhongHoc.class);
    }

    public boolean exists(String maPH) throws Exception {
        return getById(maPH) != null;
    }

    public PhongHoc create(PhongHoc phongHoc) throws Exception {
        String json = gson.toJson(phongHoc);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 409) throw new Exception("Mã phòng đã tồn tại");
        if (response.statusCode() == 422) throw new Exception("Tên phòng đã tồn tại");
        return gson.fromJson(response.body(), PhongHoc.class);
    }

    public PhongHoc update(String maPH, PhongHoc phongHoc) throws Exception {
        String json = gson.toJson(phongHoc);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maPH))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) throw new Exception("Không tìm thấy phòng học");
        return gson.fromJson(response.body(), PhongHoc.class);
    }

    public void delete(String maPH) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maPH))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) throw new Exception("Không tìm thấy phòng học");
    }

    public List<PhongHoc> search(String ma, String loai, String tinhTrang) throws Exception {
        String url = BASE_URL + "/search?ma=" + URLEncoder.encode(ma, StandardCharsets.UTF_8)
                + "&loai=" + URLEncoder.encode(loai, StandardCharsets.UTF_8)
                + "&tinhTrang=" + URLEncoder.encode(tinhTrang, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type type = new TypeToken<List<PhongHoc>>(){}.getType();
        return gson.fromJson(response.body(), type);
    }
}