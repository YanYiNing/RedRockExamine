package com.yanyining.redrockexamine.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
    private Player player = new Player();
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
        ImageView fullscreen;
        ImageView download;
        SeekBar seekBar;
        SurfaceView surfaceView;
        LinearLayout controlLayout;
        LinearLayout svLayout;

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
            fullscreen = (ImageView) view.findViewById(R.id.home_item_fullscreen);
            download = (ImageView) view.findViewById(R.id.home_item_download);
            seekBar = (SeekBar) view.findViewById(R.id.home_item_seek_bar);
            surfaceView = (SurfaceView) view.findViewById(R.id.home_item_sv);
            svLayout = (LinearLayout) view.findViewById(R.id.home_item_sv_layout);
            controlLayout = (LinearLayout) view.findViewById(R.id.home_item_control_layout);
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
        ImageLoader.build(context).bindBitmap(data.profile_image, holder.avatar,  holder.avatar.getWidth(), holder.avatar.getHeight());
        ImageLoader.build(context).bindThumbnail(data.video_uri, holder.thumbnail, holder.thumbnail.getWidth(), holder.thumbnail.getHeight());
        holder.thumbnail.setOnClickListener(new HideOrShowLayout(holder.controlLayout));
        holder.playBtn1.setOnClickListener(new PlayListener(holder.seekBar, holder.surfaceView, holder.svLayout, holder.thumbnail, holder.playBtn1, data.video_uri));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class PlayListener implements View.OnClickListener{
        private SeekBar seekBar;
        private SurfaceView surfaceView;
        private LinearLayout svLayout;
        private ImageView thumbnail;
        private ImageView playBtn1;
        String url;

        public PlayListener(SeekBar seekBar, SurfaceView surfaceView, LinearLayout svLayout, ImageView thumbnail, ImageView playBtn1, String url) {
            this.seekBar = seekBar;
            this.surfaceView = surfaceView;
            this.svLayout = svLayout;
            this.thumbnail = thumbnail;
            this.playBtn1 = playBtn1;
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            player.set(surfaceView, seekBar, svLayout);
            thumbnail.setVisibility(View.GONE);
            playBtn1.setVisibility(View.GONE);
            player.playUrl(url);
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
