package Api.Đat;

import Api.ApiConfig;
import Model.ToBoMon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder; 
import java.util.List;

public class ToHopMonApi {

    private static final String BASE_URL = ApiConfig.BASE_URL + "/api/tohopmon";
    private final Gson gson = new Gson();

    // Lấy tất cả
    public List<ToBoMon> getAll() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Type listType = new TypeToken<List<ToBoMon>>() {}.getType();
            return gson.fromJson(new InputStreamReader(conn.getInputStream()), listType);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ToBoMon> search(String keyword) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");

            URL url = new URL(BASE_URL + "/search?keyword=" + encodedKeyword);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Type listType = new TypeToken<List<ToBoMon>>() {}.getType();
                return gson.fromJson(new InputStreamReader(conn.getInputStream(), "UTF-8"), listType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }

    public boolean insert(ToBoMon toHopMon) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = gson.toJson(toHopMon);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("UTF-8"));
            }

            return conn.getResponseCode() == HttpURLConnection.HTTP_OK
                    || conn.getResponseCode() == HttpURLConnection.HTTP_CREATED;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(ToBoMon toHopMon) {
        try {
            URL url = new URL(BASE_URL + "/" + toHopMon.getMaToHop());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = gson.toJson(toHopMon);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("UTF-8"));
            }

            return conn.getResponseCode() == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maToHop) {
        try {
            URL url = new URL(BASE_URL + "/" + maToHop);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");

            return conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT
                    || conn.getResponseCode() == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}