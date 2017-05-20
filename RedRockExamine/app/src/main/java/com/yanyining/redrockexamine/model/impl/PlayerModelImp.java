package com.yanyining.redrockexamine.model.impl;

import com.yanyining.redrockexamine.presenter.impl.PlayerOnSuccessListenerImp;

/**
 * Created by YanYiNing on 2017/5/20.
 */

public interface PlayerModelImp {
    void play();
    void playUrl(String url);
    void pause();
    void stop();
    void before();
    void next();
}
