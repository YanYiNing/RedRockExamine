package com.yanyining.redrockexamine.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.yanyining.redrockexamine.R;
import com.yanyining.redrockexamine.utils.Player;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private SurfaceView sv;
    private SeekBar seekBar;
    private LinearLayout svLayout;

    private LinearLayout controlLayout;
    private FrameLayout frameLayout;
    private ImageView playBtn1;
    private ImageView playBtn2;
    private ImageView exit;
    private ImageView download;

    private String url;
    private Player player;

    private boolean isHide = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (savedInstanceState != null) {
            url = savedInstanceState.getString("Url");
        }else {
            Intent intent = getIntent();
            url = intent.getStringExtra("url");
        }

        initResource();
        initPlayer();
    }
    private void initPlayer() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        player = new Player(sv, seekBar, svLayout, width, height);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player.playUrl(url);
            }
        }, 100);
    }

    private void initResource() {

        seekBar = (SeekBar) findViewById(R.id.player_seek_bar);
        sv = (SurfaceView) findViewById(R.id.player_sv);
        svLayout = (LinearLayout) findViewById(R.id.player_sv_layout);
        controlLayout = (LinearLayout) findViewById(R.id.player_control_layout);
        playBtn1 = (ImageView) findViewById(R.id.player_play_btn1);
        playBtn2 = (ImageView) findViewById(R.id.player_play_btn2);
        exit = (ImageView) findViewById(R.id.player_fullscreen_exit);
        frameLayout = (FrameLayout) findViewById(R.id.player_frame_layout);
        download = (ImageView) findViewById(R.id.player_download);

        playBtn1.setOnClickListener(this);
        playBtn2.setOnClickListener(this);
        exit.setOnClickListener(this);
        frameLayout.setOnClickListener(this);
        download.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play_btn1:
                player.playUrl(url);
                break;
            case R.id.player_play_btn2:
                player.pause();
                break;
            case R.id.player_fullscreen_exit:
                player.stop();
                finish();
                break;
            case R.id.player_frame_layout:
                if (isHide) {
                    controlLayout.setVisibility(View.VISIBLE);
                    isHide = false;
                }else {
                    controlLayout.setVisibility(View.GONE);
                    isHide = true;
                }
                break;
            case R.id.player_download:
                player.pause();
                break;
        }
    }

    @Override
    protected void onPause() {
        player.stop();
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        player.reDraw();
        super.onConfigurationChanged(newConfig);
    }
}
