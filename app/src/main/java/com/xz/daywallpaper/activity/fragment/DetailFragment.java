package com.xz.daywallpaper.activity.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.daywallpaper.R;
import com.xz.daywallpaper.adapter.TabAdapter;
import com.xz.daywallpaper.base.BaseFragment;
import com.xz.daywallpaper.constant.Local;
import com.xz.daywallpaper.entity.PicTab;
import com.xz.daywallpaper.network.OkHttpClientManager;
import com.xz.daywallpaper.network.SignMD5;
import com.xz.daywallpaper.utils.Base64Util;
import com.xz.daywallpaper.utils.RandomUtil;
import com.xz.daywallpaper.utils.SharedPreferencesUtil;
import com.xz.daywallpaper.utils.SpacesItemDecorationVertical;
import com.youtu.Youtu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xz.daywallpaper.constant.Local.APP_ID;
import static com.xz.daywallpaper.constant.Local.SECRET_ID;
import static com.xz.daywallpaper.constant.Local.SECRET_KEY;
import static com.xz.daywallpaper.constant.Local.USER_ID;

/**
 * @author czr
 * @date 2020/3/31
 */
public class DetailFragment extends BaseFragment {
    private RecyclerView recycler;
    private TabAdapter adapter;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return true;
        }
    });

    @Override
    protected int getLayout() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initView(View rootView) {
        recycler = rootView.findViewById(R.id.recycler_tab);

    }

    @Override
    protected void initDate(Context mContext) {

        initRecycler();

    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.addItemDecoration(new SpacesItemDecorationVertical(20));
        adapter = new TabAdapter(mContext);
        recycler.setAdapter(adapter);
    }

    /**
     * 需传入图片本地地址后才开始获取图片细节信息
     *
     * @param path
     */
    public void setPicPath(String path) {
        try {
            //getPicTabV2(path);
            getVisionScreen(path);
            //getTextChat("你好呀！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 腾讯优图api
     * 识别场景标签
     */
    private void getPicTab() {
        Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY, Youtu.API_YOUTU_END_POINT, USER_ID);
        JSONObject obj = null;
        try {
//            Logger.json("标签", obj.toString());
            obj = faceYoutu.ImageTagUrl(Local.BASE_URL);
            if (obj.getInt("errorcode") == 0) {

                JSONArray array = obj.getJSONArray("tags");
                final List<PicTab> list = new ArrayList<>();
                Gson gson = new Gson();

                for (int i = 0; i < array.length(); i++) {
                    list.add(gson.fromJson(array.get(i).toString(), PicTab.class));
                }
                //保存标签数据到本地
                SharedPreferencesUtil.savePicData(mContext, Local.simTime, "tab", array.toString());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.refresh(list);
                    }
                });
//                Gson gson = new Gson();
//                view.backToUi(gson.fromJson(obj.toString(),PicTab.class));
            } else {
                mToast("AI场景标签识别错误" + obj.getString("errormsg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mToast("标签获取失败");
        }
    }

    /**
     * 腾讯Ai开放平台标签识别
     */
    private void getPicTabV2(String path) {
        String base64 = "";
        base64 = Base64Util.imageToBase64(path, "jpeg");
        Map<String, String> params = new HashMap<>();
        params.put("app_id", Local.APP_ID_TC);
        params.put("time_stamp", System.currentTimeMillis() / 1000 + "");
        params.put("nonce_str", RandomUtil.getRandomString(32));
        //======
        params.put("image", base64);//原始图片的base64编码数据（原图大小上限1MB）
        //======
        params.put("sign", SignMD5.getSignMD5(Local.APP_KEY_TC, params));

        //OkHttpClientManager.postAsyn(Local.IMAGETAB, new OkHttpClientManager.ResultCallback<String>() {
        //    @Override
        //    public void onError(Request request, Exception e) {
        //        e.printStackTrace();
        //    }
        //
        //    @Override
        //    public void onResponse(String response) {
        //        Logger.w(response);
        //    }
        //
        //}, params);
    }


    /**
     * 腾讯Ai开放平台场景识别
     *
     * @param path
     */
    private void getVisionScreen(String path) {
        String base64 = "";
        base64 = Base64Util.imageToBase64(path, "jpeg");
        Map<String, String> params = new HashMap<>();
        params.put("app_id", Local.APP_ID_TC);
        params.put("time_stamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("nonce_str", RandomUtil.getRandomString(32));
        //======
        params.put("format", "1");//图片格式
        params.put("topk", "1");//返回结果
        params.put("image", base64);//原始图片的base64编码数据（原图大小上限1MB）
        //======
        params.put("sign", SignMD5.getSignMD5(Local.APP_KEY_TC, params));

        OkHttpClientManager.postAsyn(Local.VISIONSCENER, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response) {
                Logger.w(response);
            }

        }, params);


    }


}
