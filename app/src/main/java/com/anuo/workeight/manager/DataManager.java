package com.anuo.workeight.manager;

import com.anuo.workeight.data.Item;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    public static final int requestNum = 10;
    private static final String url = "http://gank.io/api/data/Android/";
    private static DataManager instance;
    private List<Item> list = new ArrayList<>();
    private int page = 1;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void refresh(Callback callback) {
        clearData();
        loadMore(callback);
    }

    public int getCurrentPage() {
        return page - 1;
    }

    public void loadMore(Callback callback) {
        final int requestPage = page;
        HttpManager.getHttpResult(url, requestNum, requestPage, new HttpManager.HttpCallback() {
            @Override
            public void onSuccess(List<Item> list) {
                if (page == requestPage) {
                    page++;
                    DataManager.this.list.addAll(list);
                    callback.onSuccess();
                } else {
                    callback.onFail(null);
                }
            }

            @Override
            public void onFail(Exception e) {
                callback.onFail(e);
            }
        });
    }

    public void clearData() {
        list.clear();
        page = 1;
    }


    public List<Item> getList() {
        return list;
    }

    public interface Callback {
        void onSuccess();

        void onFail(Exception e);
    }

}
