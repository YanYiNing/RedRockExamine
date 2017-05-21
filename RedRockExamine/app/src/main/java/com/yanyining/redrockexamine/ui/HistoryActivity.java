package com.yanyining.redrockexamine.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.yanyining.redrockexamine.R;
import com.yanyining.redrockexamine.bean.HomeData;
import com.yanyining.redrockexamine.db.MyDatabaseHelper;
import com.yanyining.redrockexamine.ui.adapter.HomeRecyclerViewAdapter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private MyDatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private ArrayList<HomeData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initResources();
        databaseHelper = new MyDatabaseHelper(this, "Data.db", null, 2);
        db = databaseHelper.getWritableDatabase();
        getData();
        setRecyclerView(dataList);
    }

    private void getData() {
        Cursor cursor = db.query("History", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HomeData data = new HomeData();
                data.id = cursor.getInt(cursor.getColumnIndex("id"));
                data.love = cursor.getInt(cursor.getColumnIndex("love"));
                data.hate = cursor.getInt(cursor.getColumnIndex("hate"));
                data.weixin_url = cursor.getString(cursor.getColumnIndex("weixin_url"));
                data.create_time = cursor.getString(cursor.getColumnIndex("create_time"));
                data.video_uri = cursor.getString(cursor.getColumnIndex("video_uri"));
                data.text = cursor.getString(cursor.getColumnIndex("texts"));
                data.name = cursor.getString(cursor.getColumnIndex("name"));
                data.profile_image = cursor.getString(cursor.getColumnIndex("profile_image"));
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void initResources() {
        recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.home_tool_bar);
        toolbar.setTitle("百思不得姐");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
    }

    public void setRecyclerView(final ArrayList<HomeData> dataList) {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(dataList, databaseHelper);
        recyclerView.setAdapter(adapter);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(adapter.getItemViewType(0), 20);
    }
}
