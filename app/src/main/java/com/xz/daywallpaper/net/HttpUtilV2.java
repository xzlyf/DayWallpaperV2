package com.xz.daywallpaper.net;

import com.xz.daywallpaper.constant.Local;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtilV2 {


    public static void post(String method, Map<String,String> parmar, Callback callback) {

        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formbody = new FormBody.Builder();
        for (Map.Entry<String,String> entry :parmar.entrySet()){
            formbody.add(entry.getKey(),entry.getValue());
        }

//        formbody.add("name", "123");
//        formbody.add("psw", "asd");

        Request request = new Request.Builder()
                .url(Local.MSERVER_REQUEST+method)
                .post(formbody.build())
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }

}