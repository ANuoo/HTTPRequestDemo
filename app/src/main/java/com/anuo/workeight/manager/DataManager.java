package com.anuo.workeight.manager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.anuo.workeight.data.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private List<Item> list;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void initList(String str, Callback callback) {
        getHttpResult(str, callback);
    }

    private void getHttpResult(String str, Callback callback) {
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            try {
                list = new LinkedList<>();
                Log.d("title", "run: ");
                URL url = new URL(str);
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
                parseJson(builder.toString());

            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> {
                    callback.onFail(e);
                });
            }
            handler.post(callback::onSuccess);
        }).start();
    }


    private void parseJson(String data) {
        try {
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
                    imageUrl = imageUrlArray.getString(0);
                }
                list.add(i, new Item(title, user, time, imageUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getList() {
        return new LinkedList<>(list);
    }

    public interface Callback {
        void onSuccess();

        void onFail(Exception e);
    }

}
