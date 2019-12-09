package com.anuo.workeight.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.anuo.workeight.R;
import com.anuo.workeight.adapter.MainAdapter;
import com.anuo.workeight.manager.DataManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String str = "http://gank.io/api/data/Android/10/1";
        DataManager.Callback callback = new DataManager.Callback() {
            @Override
            public void onSuccess() {
                RecyclerView recyclerView = findViewById(R.id.recycler_main);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(new MainAdapter(DataManager.getInstance().getList(), MainActivity.this));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(MainActivity.this, "something wrong!", Toast.LENGTH_LONG).show();
            }
        };
        DataManager.getInstance().initList(str, callback);

    }
}
