package com.xz.daywallpaper.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class Local {
    public static String picDir = "";//本地图库地址；
    public static String picTDir = "";//当天Pic地址；

    public static String GET_SERVER_TIME="https://www.mxnzp.com/api/tools/system/time";//服务器时间
    public static final String BING_API="https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN";//服务器时间
    public static final String BING_HEAD="https://cn.bing.com";//
    public static String BASE_URL;//拼接完成的URL

    /**
     * 对话框标识
     */
    public static String DIALOG_L = "L";//加载
    public static String DIALOG_W = "W";//警告
    public static String DIALOG_E = "E";//错误
    public static String DIALOG_M = "M";//普通消息

    public static long server_time;
    public static String simTime;//20190101格式的时间

    public static class info{
        public static String enddate;
        public static String copyright;

    }
}
