package com.xz.daywallpaper.net;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xz.com.log.LogUtil;
import com.xz.daywallpaper.constant.Local;
import com.xz.daywallpaper.model.IModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

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