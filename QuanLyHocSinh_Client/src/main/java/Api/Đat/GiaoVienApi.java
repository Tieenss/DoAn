package Api.Đat;

import Api.ApiConfig;
import Model.Giaovien;
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

public class GiaoVienApi {

    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/giaovien";

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Giaovien> getAll() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Không lấy được danh sách giáo viên.");
        }

        Type type = new TypeToken<List<Giaovien>>() {}.getType();

        return gson.fromJson(response.body(), type);

    }

    public Giaovien getByMaGV(String maGV) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maGV))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            return null;
        }

        if (response.statusCode() != 200) {
            throw new Exception("Không lấy được thông tin giáo viên.");
        }

        return gson.fromJson(response.body(), Giaovien.class);
    }

    public boolean exists(String maGV) throws Exception {
        return getByMaGV(maGV) != null;
    }

    public Giaovien insert(Giaovien gv) throws Exception {

        String json = gson.toJson(gv);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Không thể thêm giáo viên.");
        }

        return gson.fromJson(response.body(), Giaovien.class);
    }

    public Giaovien update(Giaovien gv) throws Exception {

        String json = gson.toJson(gv);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + gv.getMaGV()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            throw new Exception("Không tìm thấy giáo viên.");
        }

        if (response.statusCode() != 200) {
            throw new Exception("Không thể cập nhật giáo viên.");
        }

        return gson.fromJson(response.body(), Giaovien.class);
    }

    public boolean delete(String maGV) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + maGV))
                .DELETE()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            throw new Exception("Không tìm thấy giáo viên.");
        }

        if (response.statusCode() != 204 && response.statusCode() != 200) {
            throw new Exception("Không thể xóa giáo viên.");
        }

        return true;
    }

    public List<Giaovien> searchGiaoVien(String keyword) throws Exception {

        String url = BASE_URL + "/search?keyword="
                + URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Không tìm kiếm được giáo viên.");
        }

        Type type = new TypeToken<List<Giaovien>>() {}.getType();

        return gson.fromJson(response.body(), type);
    }
}