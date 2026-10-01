package Api.HaTrang;

import Api.ApiConfig;
import Model.Phuckhao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PhucKhaoApiClient {
    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/phuckhao";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    // Lấy danh sách môn học và ghép dạng "Mã - Tên môn"
    public List<String> getAllMaMonHoc() {
        try {
            String url = ApiConfig.BASE_URL + "/api/monhoc";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 && response.body() != null) {
                List<String> listMaMH = new ArrayList<>();
                JsonArray jsonArray = gson.fromJson(response.body(), JsonArray.class);

                if (jsonArray != null) {
                    for (JsonElement element : jsonArray) {
                        if (element.isJsonObject()) {
                            JsonObject obj = element.getAsJsonObject();
                            String ma = obj.has("maMH") ? obj.get("maMH").getAsString() : "";
                            String ten = obj.has("tenMH") ? obj.get("tenMH").getAsString() : "";

                            if (!ma.isEmpty()) {
                                listMaMH.add(ten.isEmpty() ? ma : ma + " - " + ten);
                            }
                        } else if (element.isJsonPrimitive()) {
                            listMaMH.add(element.getAsString());
                        }
                    }
                }
                return listMaMH;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Phuckhao> getAll() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 && response.body() != null) {
                Type type = new TypeToken<List<Phuckhao>>(){}.getType();
                List<Phuckhao> list = gson.fromJson(response.body(), type);
                return list != null ? list : new ArrayList<>();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean insert(Phuckhao pk) {
        try {
            String json = gson.toJson(pk);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Phuckhao pk) {
        try {
            String json = gson.toJson(pk);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + pk.getMaPK()))
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

    public boolean delete(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Phuckhao> search(String keyword) {
        try {
            String url = BASE_URL + "/search?keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 && response.body() != null) {
                Type type = new TypeToken<List<Phuckhao>>(){}.getType();
                List<Phuckhao> list = gson.fromJson(response.body(), type);
                return list != null ? list : new ArrayList<>();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Phuckhao> getByMaHS(String maHS) {
        try {
            String url = BASE_URL + "/hocsinh/" + URLEncoder.encode(maHS, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200 || response.body() == null) {
                return new ArrayList<>();
            }

            String body = response.body().trim();
            Type listType = new TypeToken<List<Phuckhao>>(){}.getType();

            if (body.startsWith("{")) {
                Phuckhao singlePk = gson.fromJson(body, Phuckhao.class);
                List<Phuckhao> list = new ArrayList<>();
                if (singlePk != null) {
                    list.add(singlePk);
                }
                return list;
            }

            List<Phuckhao> list = gson.fromJson(body, listType);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}