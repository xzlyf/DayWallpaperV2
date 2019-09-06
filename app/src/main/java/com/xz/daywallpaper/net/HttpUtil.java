package com.xz.daywallpaper.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xz.daywallpaper.constant.Local;

public class HttpUtil {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(Local.MSERVER_REQUEST,params,responseHandler);
    }

    public static void post(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setConnectTimeout(5000);
        client.setTimeout(10000);
        client.setResponseTimeout(60000);
        client.post(Local.MSERVER_REQUEST+Local.MSERVER_LOGIN , params, responseHandler);
    }

    public static void meeting_post(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setConnectTimeout(5000);
        client.setTimeout(10000);
        client.setResponseTimeout(60000);
        client.post(Local.MSERVER_REQUEST , params, responseHandler);
    }


}