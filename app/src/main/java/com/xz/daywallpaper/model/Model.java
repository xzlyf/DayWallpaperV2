package com.xz.daywallpaper.model;

import com.xz.com.log.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Model implements IModel {
    @Override
    public void getDataFromNet(final String url, final OnLoadCompleteListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
//                    OkHttpClient client = new OkHttpClient();
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    listener.success(responseData);
                } catch (IOException e) {
                    LogUtil.e("请求失败链接："+url);
                    listener.failed(e);
                }
            }
        }).start();
    }


}
