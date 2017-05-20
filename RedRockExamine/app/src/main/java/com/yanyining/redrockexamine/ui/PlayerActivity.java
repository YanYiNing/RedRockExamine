package com.yanyining.redrockexamine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.yanyining.redrockexamine.R;
import com.yanyining.redrockexamine.presenter.PlayerPresenter;
import com.yanyining.redrockexamine.view.PlayerActivityImp;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, PlayerActivityImp{

    private SurfaceView sv;
    private Button btn_play, btn_pause, btn_replay, btn_stop;
    private SeekBar seekBar;
    private String url;
    private PlayerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        initResource();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        presenter = new PlayerPresenter(this, seekBar, sv, width, height);
    }
    private void initResource() {
        btn_pause = (Button) findViewById(R.id.player_pause_button);
        btn_play = (Button) findViewById(R.id.player_play_button);
        btn_replay = (Button) findViewById(R.id.player_replay_button);
        btn_stop = (Button) findViewById(R.id.player_stop_button);
        seekBar = (SeekBar) findViewById(R.id.player_seek_bar);
        sv = (SurfaceView) findViewById(R.id.player_sv);

        btn_pause.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_replay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play_button:
                presenter.playUrl(url);
                break;
            case R.id.player_pause_button:
                presenter.pause();
                break;
            case R.id.player_replay_button:

                break;
            case R.id.player_stop_button:
                presenter.stop();
                break;
        }
    }

    @Override
    protected void onPause() {
        presenter.stop();
        super.onPause();
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void changePlayButton() {

    }


    @Override
    public void setSurfaceView() {

    }
}
