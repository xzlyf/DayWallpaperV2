package com.xz.daywallpaper.model;

public interface IModel {
    //从网络上获取数据
    void getDataFromNet(String url, OnLoadCompleteListener listener);

    //回调接口
    interface OnLoadCompleteListener {
        void success(String data);

        void failed(Exception e);
    }

    //回调接口2.0
    interface OnReady{
        void finish();
        void failed();
    }
}
