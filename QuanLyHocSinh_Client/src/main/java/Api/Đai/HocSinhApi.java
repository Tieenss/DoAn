package Api.Đai;

import Api.ApiConfig;
import Model.HocSinh;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class HocSinhApi {

    private static final String API_URL = ApiConfig.BASE_URL+ "/api/hocsinh";

    private static final Gson gson = new Gson();

    public List<HocSinh> getAllHocSinh() {

        try {
            URL url = new URL(API_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));

            Type listType = new TypeToken<List<HocSinh>>(){}.getType();

            List<HocSinh> ds = gson.fromJson(br, listType);

            br.close();
            conn.disconnect();

            return ds;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HocSinh getHocSinh(String maHS) {

        try {

            URL url = new URL(API_URL + "/" + maHS);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));

            HocSinh hs = gson.fromJson(br, HocSinh.class);

            br.close();
            conn.disconnect();

            return hs;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertHocSinh(HocSinh hs) {

        try {

            URL url = new URL(API_URL);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();

            os.write(gson.toJson(hs).getBytes("UTF-8"));

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

    public boolean updateHocSinh(HocSinh hs) {

        try {

            URL url = new URL(API_URL + "/" + hs.getMaHS());

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");

            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();

            os.write(gson.toJson(hs).getBytes("UTF-8"));

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

    public boolean deleteHocSinh(String maHS) {

        try {

            URL url = new URL(API_URL + "/" + maHS);

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

    public List<HocSinh> search(String keyword) {

        try {

            URL url = new URL(API_URL + "/search?keyword=" + URLEncoder.encode(keyword, "UTF-8"));

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            Type type = new TypeToken<List<HocSinh>>(){}.getType();

            List<HocSinh> list = gson.fromJson(br, type);

            br.close();
            conn.disconnect();

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getAllMaLop() {

        try {

            URL url = new URL(API_URL + "/malop");

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            Type type = new TypeToken<List<String>>(){}.getType();

            List<String> list = gson.fromJson(br, type);

            br.close();
            conn.disconnect();

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getAllMaDT() {

        try {

            URL url = new URL(API_URL + "/madoituong");

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            Type type = new TypeToken<List<String>>(){}.getType();

            List<String> list = gson.fromJson(br, type);

            br.close();
            conn.disconnect();

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}