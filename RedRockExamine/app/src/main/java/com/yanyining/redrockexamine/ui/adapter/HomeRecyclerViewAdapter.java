package com.yanyining.redrockexamine.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.yanyining.redrockexamine.db.MyDatabaseHelper;
import com.yanyining.redrockexamine.ui.PlayerActivity;
import com.yanyining.redrockexamine.utils.Player;
import com.yanyining.redrockexamine.utils.downloadtools.DownloadService;
import com.yanyining.redrockexamine.utils.imageTools.ImageLoader;

import java.util.ArrayList;

/**
 * Created by YanYiNing on 2017/5/20.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    ArrayList<HomeData> dataList;
    private MyDatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Context context;
    private Player player;
    boolean isLayoutHide = true;
    private DownloadService.DownloadBinder downloadBinder;

    public HomeRecyclerViewAdapter(ArrayList<HomeData> dataList, MyDatabaseHelper databaseHelper, DownloadService.DownloadBinder downloadBinder) {
        this.dataList = dataList;
        this.databaseHelper = databaseHelper;
        db = databaseHelper.getWritableDatabase();
        player = new Player(db);
        this.downloadBinder = downloadBinder;
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
        holder.fullscreen.setOnClickListener(new ToPlayerActivity(data.video_uri, holder.thumbnail, holder.playBtn1, holder.playBtn2));
        holder.url = data.video_uri;
        ImageLoader.build(context).bindBitmap(data.profile_image, holder.avatar,  holder.avatar.getWidth(), holder.avatar.getHeight());
        ImageLoader.build(context).bindThumbnail(data.video_uri, holder.thumbnail, holder.thumbnail.getWidth(), holder.thumbnail.getHeight());
        holder.frameLayout.setOnClickListener(new HideOrShowLayout(holder.controlLayout));
        holder.playBtn1.setOnClickListener(new PlayBtn1Listener(holder.seekBar, holder.surfaceView, holder.svLayout, holder.thumbnail, holder.playBtn1, holder.playBtn2, data));
        holder.playBtn2.setOnClickListener(new PlayBtn2Listener(holder.seekBar, holder.surfaceView, holder.svLayout, holder.thumbnail, holder.playBtn1, holder.playBtn2, data));
        holder.download.setOnClickListener(new DownloadListener(holder.url));
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

    class DownloadListener implements View.OnClickListener{

        private String url;

        public DownloadListener(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            downloadBinder.startDownload(url);
        }
    }
    class PlayBtn1Listener implements View.OnClickListener{
        private SeekBar seekBar;
        private SurfaceView surfaceView;
        private LinearLayout svLayout;
        private ImageView thumbnail;
        private ImageView playBtn1;
        private ImageView playBtn2;
        private HomeData data;

        public PlayBtn1Listener(SeekBar seekBar, SurfaceView surfaceView, LinearLayout svLayout, ImageView thumbnail, ImageView playBtn1, ImageView playBtn2, HomeData data) {
            this.seekBar = seekBar;
            this.surfaceView = surfaceView;
            this.svLayout = svLayout;
            this.thumbnail = thumbnail;
            this.playBtn1 = playBtn1;
            this.playBtn2 = playBtn2;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            writeToHistory(db, data);
            if(!data.video_uri.equals(player.url)&&player.url!=null){
                player.stop();
            }
            if (!player.initPlay) {
                playBtn2.setImageResource(R.drawable.pause);
                player.set(surfaceView, seekBar, svLayout);
                thumbnail.setVisibility(View.GONE);
                playBtn1.setVisibility(View.GONE);
                player.playUrl(data.video_uri, 0);
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
        private HomeData data;

        public PlayBtn2Listener(SeekBar seekBar, SurfaceView surfaceView, LinearLayout svLayout, ImageView thumbnail, ImageView playBtn1, ImageView playBtn2, HomeData data) {
            this.seekBar = seekBar;
            this.surfaceView = surfaceView;
            this.svLayout = svLayout;
            this.thumbnail = thumbnail;
            this.playBtn1 = playBtn1;
            this.playBtn2 = playBtn2;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            writeToHistory(db, data);
            if(!data.video_uri.equals(player.url)&&player.url!=null){
                player.stop();
            }
            if (!player.initPlay) {
                playBtn2.setImageResource(R.drawable.pause);
                player.set(surfaceView, seekBar, svLayout);
                thumbnail.setVisibility(View.GONE);
                playBtn1.setVisibility(View.GONE);
                player.playUrl(data.video_uri, 0);
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
        ImageView thumbnail;
        ImageView playBtn1;
        ImageView playBtn2;

        public ToPlayerActivity(String url, ImageView thumbnail, ImageView playBtn1, ImageView playBtn2) {
            this.url = url;
            this.thumbnail = thumbnail;
            this.playBtn1 = playBtn1;
            this.playBtn2 = playBtn2;
        }

        @Override
        public void onClick(View v) {
            thumbnail.setVisibility(View.VISIBLE);
            playBtn1.setVisibility(View.VISIBLE);
            playBtn2.setImageResource(R.drawable.play_btn2);
            int progress = player.getProgress();
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("progress", progress);
            context.startActivity(intent);
            player.stop();
        }
    }

    private void writeToHistory(SQLiteDatabase db, HomeData data){
        if (!search(data.id)) {
            ContentValues values = new ContentValues();
            values.put("hate", data.hate);
            values.put("weixin_url", data.weixin_url);
            values.put("profile_image", data.profile_image);
            values.put("love", data.love);
            values.put("name", data.name);
            values.put("create_time", data.create_time);
            values.put("video_uri", data.video_uri);
            values.put("texts", data.text);
            values.put("id", data.id);
            db.insert("History", null, values);
            values.clear();
        }
    }

    private boolean search(int id) {
        Cursor cursor = db.query("History", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int ID = cursor.getInt(cursor.getColumnIndex("id"));
                if (id == ID){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

}
