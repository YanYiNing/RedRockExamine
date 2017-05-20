package com.yanyining.redrockexamine.presenter;

import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.widget.SeekBar;

import com.yanyining.redrockexamine.model.PlayerModel;
import com.yanyining.redrockexamine.presenter.impl.PlayerPresenterImp;
import com.yanyining.redrockexamine.presenter.impl.PlayerOnSuccessListenerImp;
import com.yanyining.redrockexamine.ui.PlayerActivity;

/**
 * Created by YanYiNing on 2017/5/20.
 */

public class PlayerPresenter implements PlayerPresenterImp, PlayerOnSuccessListenerImp {
    private PlayerActivity view;
    private PlayerModel model;

    public PlayerPresenter(PlayerActivity view, SeekBar seekBar, SurfaceView sv, int weight, int height) {
        this.view = view;
        model = new PlayerModel(sv, seekBar, weight, height, this);
    }

    @Override
    public void playUrl(String url) {
        model.playUrl(url);
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {
        model.pause();
    }

    @Override
    public void stop() {
        model.stop();
    }

    @Override
    public void before() {
        model.before();
    }

    @Override
    public void next() {
        model.next();
    }

    @Override
    public int getHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        view.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mSurfaceViewWidth = dm.widthPixels;
        int mSurfaceViewHeight = dm.heightPixels;
        return mSurfaceViewHeight;
    }

    @Override
    public PlayerActivity getView() {
        return view;
    }
}