package com.yanyining.redrockexamine.presenter;

import com.yanyining.redrockexamine.bean.HomeData;
import com.yanyining.redrockexamine.db.MyDatabaseHelper;
import com.yanyining.redrockexamine.model.HomeModel;
import com.yanyining.redrockexamine.presenter.impl.HomeOnSucessListener;
import com.yanyining.redrockexamine.presenter.impl.HomePresenterImp;
import com.yanyining.redrockexamine.ui.HomeActivity;

import java.util.ArrayList;

/**
 * Created by YanYiNing on 2017/5/20.
 */

public class HomePresenter implements HomePresenterImp, HomeOnSucessListener {
    private MyDatabaseHelper databaseHelper;
    HomeModel model;
    HomeActivity view;

    public HomePresenter(HomeActivity view) {
        this.view = view;
        databaseHelper = new MyDatabaseHelper(view, "Data.db", null, 2);
        model = new HomeModel(databaseHelper);
    }

    @Override
    public void getData() {
        model.getData(this);
    }


    @Override
    public void onSuccess(final ArrayList<HomeData> dataList) {
        view.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setRecyclerView(dataList);
            }
        });
    }
}
