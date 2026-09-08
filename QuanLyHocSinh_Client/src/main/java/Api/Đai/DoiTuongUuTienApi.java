package Api.Đai;

import Api.ApiConfig;
import Model.DoiTuongUuTien;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class DoiTuongUuTienApi {

    private static final String API_URL = ApiConfig.BASE_URL+ "/api/doituong";

    private static final Gson gson = new Gson();

    public List<DoiTuongUuTien> getAll() {

        try {

            URL url = new URL(API_URL);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));

            Type type = new TypeToken<List<DoiTuongUuTien>>() {}.getType();

            List<DoiTuongUuTien> list = gson.fromJson(br, type);

            br.close();
            conn.disconnect();

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public DoiTuongUuTien getById(String maDT) {

        try {

            URL url = new URL(API_URL + "/" + maDT);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));

            DoiTuongUuTien dt = gson.fromJson(br, DoiTuongUuTien.class);

            br.close();
            conn.disconnect();

            return dt;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insert(DoiTuongUuTien dt) {

        try {

            URL url = new URL(API_URL);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();

            os.write(gson.toJson(dt).getBytes("UTF-8"));

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            conn.disconnect();

            return responseCode == HttpURLConnection.HTTP_OK
                    || responseCode == HttpURLConnection.HTTP_CREATED;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(DoiTuongUuTien dt) {

        try {

            URL url = new URL(API_URL + "/" + dt.getMaDT());

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();

            os.write(gson.toJson(dt).getBytes("UTF-8"));

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            conn.disconnect();

            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(String maDT) {

        try {

            URL url = new URL(API_URL + "/" + maDT);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();

            conn.disconnect();

            return responseCode == HttpURLConnection.HTTP_OK
                    || responseCode == HttpURLConnection.HTTP_NO_CONTENT;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<DoiTuongUuTien> search(String keyword) {

        try {

            URL url = new URL(API_URL + "/search?keyword="
                    + URLEncoder.encode(keyword, "UTF-8"));

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));

            Type type = new TypeToken<List<DoiTuongUuTien>>() {}.getType();

            List<DoiTuongUuTien> list = gson.fromJson(br, type);

            br.close();
            conn.disconnect();

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}