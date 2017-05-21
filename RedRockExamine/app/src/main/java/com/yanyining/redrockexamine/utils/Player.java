package com.yanyining.redrockexamine.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by YanYiNing on 2017/5/20.
 */

public class Player implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener,
        SurfaceHolder.Callback {

    int mSurfaceViewWidth, mSurfaceViewHeight;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;

    private SeekBar skbProgress;
    private LinearLayout linearLayout;

    private Timer mTimer = new Timer();
    private int lw = 1080;
    private int lh = 1920;
    boolean isPlaying = false;
    public boolean initPlay = false;
    public String url;

    public Player(){
        mTimer.schedule(new mTimerTask(), 0, 1000);
    }

    public void set(SurfaceView surfaceView, SeekBar skbProgress, LinearLayout linearLayout){
        mSurfaceViewWidth = 1080;
        mSurfaceViewHeight = 1920;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        this.skbProgress = skbProgress;
        skbProgress.setOnSeekBarChangeListener(change);
        this.surfaceView = surfaceView;
        this.linearLayout = linearLayout;
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
        Log.e("mediaPlayer", "surface created");
        initPlay = true;
        mTimer.schedule(new mTimerTask() , 0, 1000);
    }

    public Player(SurfaceView surfaceView, SeekBar skbProgress, LinearLayout linearLayout, int weight, int height)
    {
        mSurfaceViewWidth = weight;
        mSurfaceViewHeight = height;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        this.skbProgress = skbProgress;
        skbProgress.setOnSeekBarChangeListener(change);
        this.surfaceView = surfaceView;
        this.linearLayout = linearLayout;
    }
    class mTimerTask extends TimerTask{

        @Override
        public void run() {
            if(mediaPlayer==null)
                return;
            else{
                if (mediaPlayer.isPlaying()) {
                    if(skbProgress != null)
                        if(skbProgress.isPressed() == false)
                            handleProgress.sendEmptyMessage(0);
                }
            }
        }
    }
    /*TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if(mediaPlayer==null)
                return;
            if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };*/

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            if(mediaPlayer != null) {
                int position = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if (duration > 0) {
                    long pos = skbProgress.getMax() * position / duration;
                    skbProgress.setProgress((int) pos);
                }
            }
        };
    };

    public void reDraw(){

        int vWidth = mediaPlayer.getVideoWidth();
        int vHeight = mediaPlayer.getVideoHeight();

        // 该LinearLayout的父容器 android:orientation="vertical" 必须
        lw = lw + lh;
        lh = lw - lh;
        lw = lw - lh;

        // 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
        float wRatio = (float) vWidth / (float) lw;
        float hRatio = (float) vHeight / (float) lh;

        // 选择大的一个进行缩放
        float ratio = Math.max(wRatio, hRatio);
        vWidth = (int) Math.ceil((float) vWidth / ratio);
        vHeight = (int) Math.ceil((float) vHeight / ratio);

        // 设置surfaceView的布局参数
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) surfaceView.getLayoutParams();

        if (wRatio > hRatio){
            int margin = (lh - vHeight) / 2;
            lp.setMargins(0, margin, 0, margin);
        } else {
            int margin = (lw - vWidth) / 2;
            lp.setMargins(margin, 0, margin, 0);
        }

    }
    public boolean isPlaying(){
        return isPlaying;
    }

    public void play()
    {
        mediaPlayer.start();
        isPlaying = true;
    }

    public void playUrl(String videoUrl)
    {
        url = videoUrl;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepareAsync();//prepare之后自动播放

            //mediaPlayer.start();

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
    }

    public void pause()
    {
        mediaPlayer.pause();
        isPlaying = false;
    }

    public void stop()
    {
        while (mediaPlayer != null) {
            url = null;
            initPlay = false;
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            Log.d(TAG, "stop: ");
        }
    }

    private SeekBar.OnSeekBarChangeListener change = new SeekBar.OnSeekBarChangeListener() {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()

            this.progress = progress * mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字

            mediaPlayer.seekTo(progress);
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
        Log.e("mediaPlayer", "surface created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }


    @Override
    public void onPrepared(MediaPlayer arg0) {
        int vWidth = mediaPlayer.getVideoWidth();
        int vHeight = mediaPlayer.getVideoHeight();

        // 该LinearLayout的父容器 android:orientation="vertical" 必须
        int lw = linearLayout.getWidth();
        int lh = linearLayout.getHeight();

        // 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
        float wRatio = (float) vWidth / (float) lw;
        float hRatio = (float) vHeight / (float) lh;

        // 选择大的一个进行缩放
        float ratio = Math.max(wRatio, hRatio);
        vWidth = (int) Math.ceil((float) vWidth / ratio);
        vHeight = (int) Math.ceil((float) vHeight / ratio);

        // 设置surfaceView的布局参数
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) surfaceView.getLayoutParams();

        if (wRatio > hRatio){
            int margin = (lh - vHeight) / 2;
            lp.setMargins(0, margin, 0, margin);
        } else {
            int margin = (lw - vWidth) / 2;
            lp.setMargins(margin, 0, margin, 0);
        }

        surfaceView.setLayoutParams(lp);

        arg0.start();
        isPlaying = true;
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        if(mediaPlayer != null) {
            skbProgress.setSecondaryProgress(bufferingProgress);
            int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
            Log.e(currentProgress + "% play_btn1", bufferingProgress + "% buffer");
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mediaPlayer.release();
        return false;
    }


}
