package com.yanyining.redrockexamine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.yanyining.redrockexamine.R;
import com.yanyining.redrockexamine.utils.Player;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private SurfaceView sv;
    private Button btn_play, btn_pause, btn_replay, btn_stop;
    private SeekBar seekBar;
    private LinearLayout linearLayout;

    private String url;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        initResource();
        initPlayer();

    }
    private void initPlayer() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        player = new Player(sv, seekBar, linearLayout, width, height);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player.playUrl(url);
            }
        }, 100);
    }

    private void initResource() {
        btn_pause = (Button) findViewById(R.id.player_pause_button);
        btn_play = (Button) findViewById(R.id.player_play_button);
        btn_replay = (Button) findViewById(R.id.player_replay_button);
        btn_stop = (Button) findViewById(R.id.player_stop_button);
        seekBar = (SeekBar) findViewById(R.id.player_seek_bar);
        sv = (SurfaceView) findViewById(R.id.player_sv);
        linearLayout = (LinearLayout) findViewById(R.id.player_layout);

        btn_pause.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_replay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play_button:
                player.playUrl(url);
                break;
            case R.id.player_pause_button:
                player.pause();
                break;
            case R.id.player_replay_button:

                break;
            case R.id.player_stop_button:
                player.stop();
                break;
        }
    }

    @Override
    protected void onPause() {
        player.stop();
        super.onPause();
    }

}
