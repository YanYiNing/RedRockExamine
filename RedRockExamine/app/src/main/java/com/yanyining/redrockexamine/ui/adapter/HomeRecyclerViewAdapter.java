package com.yanyining.redrockexamine.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanyining.redrockexamine.R;
import com.yanyining.redrockexamine.bean.HomeData;
import com.yanyining.redrockexamine.utils.imageTools.ImageLoader;

import java.util.ArrayList;

/**
 * Created by YanYiNing on 2017/5/20.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    ArrayList<HomeData> dataList;
    private Context context;

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

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.home_item_user_name);
            time = (TextView) view.findViewById(R.id.home_item_time);
            avatar = (ImageView) view.findViewById(R.id.home_item_avatar);
            zan = (TextView) view.findViewById(R.id.home_item_zan);
            cai = (TextView) view.findViewById(R.id.home_item_cai);
            text = (TextView) view.findViewById(R.id.home_item_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
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
        ImageLoader.build(context).bindBitmap(data.profile_image, holder.avatar,  holder.avatar.getWidth(), holder.avatar.getHeight());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
