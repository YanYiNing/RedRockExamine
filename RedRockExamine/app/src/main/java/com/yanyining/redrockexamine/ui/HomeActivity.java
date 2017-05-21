package com.yanyining.redrockexamine.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yanyining.redrockexamine.R;
import com.yanyining.redrockexamine.bean.HomeData;
import com.yanyining.redrockexamine.db.MyDatabaseHelper;
import com.yanyining.redrockexamine.presenter.HomePresenter;
import com.yanyining.redrockexamine.ui.adapter.HomeRecyclerViewAdapter;
import com.yanyining.redrockexamine.utils.downloadtools.DownloadService;
import com.yanyining.redrockexamine.view.HomeActivityImp;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeActivityImp {
    private HomePresenter presenter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private MyDatabaseHelper databaseHelper;
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        initResources();
        databaseHelper = new MyDatabaseHelper(this, "Data.db", null, 2);
        presenter = new HomePresenter(this);
        presenter.getData();
    }

    private void initResources() {
        recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.home_tool_bar);
        toolbar.setTitle("百思不得姐");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.inflateMenu(R.menu.tool_bar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_history:
                        Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                        startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    public void setRecyclerView(final ArrayList<HomeData> dataList) {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(dataList, databaseHelper, downloadBinder);
        recyclerView.setAdapter(adapter);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(adapter.getItemViewType(0), 20);
    }

}
