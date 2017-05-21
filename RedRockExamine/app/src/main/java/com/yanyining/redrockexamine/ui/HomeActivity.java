package com.yanyining.redrockexamine.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.yanyining.redrockexamine.view.HomeActivityImp;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeActivityImp {
    private HomePresenter presenter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(dataList, databaseHelper);
        recyclerView.setAdapter(adapter);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(adapter.getItemViewType(0), 20);
    }

}
