package com.yanyining.redrockexamine.model;

import com.yanyining.redrockexamine.bean.HomeData;
import com.yanyining.redrockexamine.model.impl.HomeModelImp;
import com.yanyining.redrockexamine.presenter.impl.HomeOnSucessListener;
import com.yanyining.redrockexamine.utils.downloadtools.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by YanYiNing on 2017/5/20.
 */

public class HomeModel implements HomeModelImp {
    private String url = "http://route.showapi.com/255-1";
    private String body;
    private String params = "showapi_appid=35847"
            + "&showapi_sign=b23a19aef9bb4411ba45d170cce6d476"
            + "&type=41"
            + "&page=1";

    private ArrayList<HomeData> dataList = new ArrayList<>();
    @Override
    public void getData(final HomeOnSucessListener listener) {
        try {
            HttpUtils.doPostAsyn(url, params, new HttpUtils.CallBack() {
                @Override
                public void onRequestComplete(String result) {
                    transform(result);
                    listener.onSuccess(dataList);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transform(String result) {
        try{
            JSONObject jsonObject = new JSONObject(result);
            body = jsonObject.getString("showapi_res_body");
            jsonObject = new JSONObject(body);
            body = jsonObject.getString("pagebean");
            jsonObject = new JSONObject(body);
            body = jsonObject.getString("contentlist");
            JSONArray jsonArray = new JSONArray(body);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                HomeData data = new HomeData();
                data.hate = jsonObject.getInt("hate");
                data.love = jsonObject.getInt("love");
                data.create_time = jsonObject.getString("create_time");
                data.name = jsonObject.getString("name");
                data.profile_image = jsonObject.getString("profile_image");
                data.video_uri = jsonObject.getString("video_uri");
                data.weixin_url = jsonObject.getString("weixin_url");
                dataList.add(data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
