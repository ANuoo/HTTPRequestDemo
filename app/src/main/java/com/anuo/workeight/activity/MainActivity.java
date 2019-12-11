package com.anuo.workeight.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.Toast;

import com.anuo.workeight.R;
import com.anuo.workeight.adapter.MainAdapter;
import com.anuo.workeight.manager.DataManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainAdapter adapter;
    private DataManager.Callback refreshCallback = new DataManager.Callback() {
        @Override
        public void onSuccess() {
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFail(Exception e) {
            Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textTitle = findViewById(R.id.txt_top_title);
        TextView textTime = findViewById(R.id.txt_top_time);
        textTime.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis())));
        swipeRefreshLayout = findViewById(R.id.swipe_main);
        swipeRefreshLayout.setOnRefreshListener(this);
        textTitle.setText(setView());
        DataManager.getInstance().refresh(new DataManager.Callback() {
            @Override
            public void onSuccess() {
                initView();
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "something wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String setView() {
        Time time = new Time();
        time.setToNow();
        return (time.hour >= 5 && time.hour < 18 ? "Hi,goodMorning!" : "Hi,goodEvening!");
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new MainAdapter(DataManager.getInstance().getList(), MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        DataManager.getInstance().refresh(refreshCallback);
    }


}