package com.anuo.workeight.manager;

import android.os.Handler;
import android.os.Looper;

import com.anuo.workeight.data.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpManager {
    public static void getHttpResult(String urlStr, int requestNum, int page, HttpCallback callback) {
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            try {
                URL url = new URL(urlStr + requestNum + "/" + page);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String temp;
                while ((temp = reader.readLine()) != null) {
                    builder.append(temp);
                }
                List<Item> list = parseJson(builder.toString());

                handler.post(() -> {
                    callback.onSuccess(list);
                });
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> {
                    callback.onFail(e);
                });
            }
        }).start();
    }


    private static List<Item> parseJson(String data) throws JSONException {
        List<Item> list = new ArrayList<>();
        JSONObject object = new JSONObject(data);
        JSONArray array = object.getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String title = jsonObject.getString("desc");
            String user = jsonObject.getString("who");
            String time = jsonObject.getString("publishedAt").split("T")[0];
            String imageUrl = null;
            if (jsonObject.has("images")) {
                JSONArray imageUrlArray = jsonObject.getJSONArray("images");
                if (imageUrlArray.length() != 0) {
                    imageUrl = imageUrlArray.getString(0);
                }
            }
            list.add(new Item(title, user, time, imageUrl));
        }
        return list;
    }

    interface HttpCallback {
        void onSuccess(List<Item> list);

        void onFail(Exception e);
    }
}
