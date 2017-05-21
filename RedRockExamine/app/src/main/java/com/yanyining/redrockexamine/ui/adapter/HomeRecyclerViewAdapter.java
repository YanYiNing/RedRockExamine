package com.yanyining.redrockexamine.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yanyining.redrockexamine.R;
import com.yanyining.redrockexamine.bean.HomeData;
import com.yanyining.redrockexamine.ui.PlayerActivity;
import com.yanyining.redrockexamine.utils.Player;
import com.yanyining.redrockexamine.utils.imageTools.ImageLoader;

import java.util.ArrayList;

/**
 * Created by YanYiNing on 2017/5/20.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    ArrayList<HomeData> dataList;
    private Context context;
    private static Player player = new Player();
    boolean isLayoutHide = true;

    public HomeRecyclerViewAdapter(ArrayList<HomeData> dataList) {
        this.dataList = dataList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView time;
        TextView cai;
        TextView zan;
        TextView text;
        ImageView avatar;
        ImageView thumbnail;
        ImageView playBtn1;
        ImageView playBtn2;
        ImageView fullscreen;
        ImageView download;
        SeekBar seekBar;
        SurfaceView surfaceView;
        LinearLayout controlLayout;
        LinearLayout svLayout;
        FrameLayout frameLayout;
        String url;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.home_item_user_name);
            time = (TextView) view.findViewById(R.id.home_item_time);
            avatar = (ImageView) view.findViewById(R.id.home_item_avatar);
            zan = (TextView) view.findViewById(R.id.home_item_zan);
            cai = (TextView) view.findViewById(R.id.home_item_cai);
            text = (TextView) view.findViewById(R.id.home_item_text);
            thumbnail = (ImageView) view.findViewById(R.id.home_item_thumbnail);
            playBtn1 = (ImageView) view.findViewById(R.id.home_item_play_btn1);
            playBtn2 = (ImageView) view.findViewById(R.id.home_item_play_btn2);
            fullscreen = (ImageView) view.findViewById(R.id.home_item_fullscreen);
            download = (ImageView) view.findViewById(R.id.home_item_download);
            seekBar = (SeekBar) view.findViewById(R.id.home_item_seek_bar);
            surfaceView = (SurfaceView) view.findViewById(R.id.home_item_sv);
            svLayout = (LinearLayout) view.findViewById(R.id.home_item_sv_layout);
            controlLayout = (LinearLayout) view.findViewById(R.id.home_item_control_layout);
            frameLayout = (FrameLayout) view.findViewById(R.id.home_item_frame_layout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null) {
            context = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeData data = dataList.get(position);
        holder.username.setText(data.name);
        holder.time.setText(data.create_time);
        holder.zan.setText(String.valueOf(data.love));
        holder.cai.setText(String.valueOf(data.hate));
        holder.text.setText(data.text);
        holder.fullscreen.setOnClickListener(new ToPlayerActivity(data.video_uri));
        holder.url = data.video_uri;
        ImageLoader.build(context).bindBitmap(data.profile_image, holder.avatar,  holder.avatar.getWidth(), holder.avatar.getHeight());
        ImageLoader.build(context).bindThumbnail(data.video_uri, holder.thumbnail, holder.thumbnail.getWidth(), holder.thumbnail.getHeight());
        holder.frameLayout.setOnClickListener(new HideOrShowLayout(holder.controlLayout));
        holder.playBtn1.setOnClickListener(new PlayBtn1Listener(holder.seekBar, holder.surfaceView, holder.svLayout, holder.thumbnail, holder.playBtn1, holder.playBtn2, holder.url));
        holder.playBtn2.setOnClickListener(new PlayBtn2Listener(holder.seekBar, holder.surfaceView, holder.svLayout, holder.thumbnail, holder.playBtn1, holder.playBtn2, holder.url));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.thumbnail.setVisibility(View.VISIBLE);
        holder.playBtn1.setVisibility(View.VISIBLE);
        if(holder.url.equals(player.url)) {
            player.stop();
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {

        super.onViewRecycled(holder);
    }

    class PlayBtn1Listener implements View.OnClickListener{
        private SeekBar seekBar;
        private SurfaceView surfaceView;
        private LinearLayout svLayout;
        private ImageView thumbnail;
        private ImageView playBtn1;
        private ImageView playBtn2;
        String url;

        public PlayBtn1Listener(SeekBar seekBar, SurfaceView surfaceView, LinearLayout svLayout, ImageView thumbnail, ImageView playBtn1, ImageView playBtn2, String url) {
            this.seekBar = seekBar;
            this.surfaceView = surfaceView;
            this.svLayout = svLayout;
            this.thumbnail = thumbnail;
            this.playBtn1 = playBtn1;
            this.playBtn2 = playBtn2;
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            if(!url.equals(player.url)){
                player.stop();
            }
            if (!player.initPlay) {
                playBtn2.setImageResource(R.drawable.pause);
                player.set(surfaceView, seekBar, svLayout);
                thumbnail.setVisibility(View.GONE);
                playBtn1.setVisibility(View.GONE);
                player.playUrl(url);
            } else {
                player.play();
                playBtn1.setVisibility(View.GONE);
                playBtn2.setImageResource(R.drawable.pause);
            }

        }
    }
    class PlayBtn2Listener implements View.OnClickListener{
        private SeekBar seekBar;
        private SurfaceView surfaceView;
        private LinearLayout svLayout;
        private ImageView thumbnail;
        private ImageView playBtn1;
        private ImageView playBtn2;
        String url;

        public PlayBtn2Listener(SeekBar seekBar, SurfaceView surfaceView, LinearLayout svLayout, ImageView thumbnail, ImageView playBtn1, ImageView playBtn2, String url) {
            this.seekBar = seekBar;
            this.surfaceView = surfaceView;
            this.svLayout = svLayout;
            this.thumbnail = thumbnail;
            this.playBtn1 = playBtn1;
            this.playBtn2 = playBtn2;
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            if(!url.equals(player.url)){
                player.stop();
            }
            if (!player.initPlay) {
                playBtn2.setImageResource(R.drawable.pause);
                player.set(surfaceView, seekBar, svLayout);
                thumbnail.setVisibility(View.GONE);
                playBtn1.setVisibility(View.GONE);
                player.playUrl(url);
            } else {
                if(player.isPlaying()) {
                    player.pause();
                    playBtn2.setImageResource(R.drawable.play_btn2);
                    playBtn1.setVisibility(View.VISIBLE);
                } else {
                    player.play();
                    playBtn2.setImageResource(R.drawable.pause);
                    playBtn1.setVisibility(View.GONE);
                }
            }

        }
    }
    class HideOrShowLayout implements View.OnClickListener{
        LinearLayout linearLayout;

        public HideOrShowLayout(LinearLayout linearLayout) {
            this.linearLayout = linearLayout;
        }

        @Override
        public void onClick(View v) {
            if(isLayoutHide){
                linearLayout.setVisibility(View.VISIBLE);
                isLayoutHide = false;
            } else {
                linearLayout.setVisibility(View.GONE);
                isLayoutHide = true;
            }
        }
    }
    class ToPlayerActivity implements View.OnClickListener{
        String url;

        public ToPlayerActivity(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

}
