package com.yanyining.redrockexamine.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.yanyining.redrockexamine.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_address;
    private SurfaceView sv;
    private Button btn_play, btn_pause, btn_replay, btn_stop;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initResource();


        player = new Player(sv, seekBar);
    }

    private void initResource() {
        edit_address = (EditText) findViewById(R.id.main_address_edit_text);
        btn_pause = (Button) findViewById(R.id.main_pause_button);
        btn_play = (Button) findViewById(R.id.main_play_button);
        btn_replay = (Button) findViewById(R.id.main_replay_button);
        btn_stop = (Button) findViewById(R.id.main_stop_button);
        seekBar = (SeekBar) findViewById(R.id.main_seek_bar);
        sv = (SurfaceView) findViewById(R.id.main_sv);
        mediaPlayer = new MediaPlayer();

        btn_pause.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_replay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_play_button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        player.playUrl("http://mvideo.spriteapp.cn/video/2017/0517/27acb7cc-3b15-11e7-b133-1866daeb0df1_wpc.mp4");
                    }
                }).start();

                break;
            case R.id.main_pause_button:

                break;
            case R.id.main_replay_button:

                break;
            case R.id.main_stop_button:

                break;
        }
    }


}

