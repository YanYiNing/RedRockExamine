package com.yanyining.redrockexamine.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yanyining.redrockexamine.R;
import com.yanyining.redrockexamine.bean.HomeData;
import com.yanyining.redrockexamine.presenter.HomePresenter;
import com.yanyining.redrockexamine.ui.adapter.HomeRecyclerViewAdapter;
import com.yanyining.redrockexamine.view.HomeActivityImp;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeActivityImp {
    private HomePresenter presenter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initResources();
        presenter = new HomePresenter(this);
        presenter.getData();
    }

    private void initResources() {
        recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
    }

    @Override
    public void setRecyclerView(final ArrayList<HomeData> dataList) {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(adapter.getItemViewType(0), 20);
    }

}
