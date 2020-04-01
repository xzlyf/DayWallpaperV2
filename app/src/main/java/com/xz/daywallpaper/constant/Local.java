package com.xz.daywallpaper.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class Local {
    public static String picDir = "";//本地图库地址；
    public static String picTDir = "";//当天Pic地址；

    public static String GET_SERVER_TIME = "https://www.mxnzp.com/api/tools/system/time?app_id=phfpqhngvmlpjohj&app_secret=cVVYQktmR3JwSEIyNkNPT1g5SGQ0UT09";//服务器时间
    public static final String BING_API = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN";
    public static final String UPDATE_SERVER = "http://www.xzlyf.club/DayWallpaper/update.json";//默认更新地址

    public static final String BING_HEAD = "https://cn.bing.com";//
    public static String BASE_URL;//拼接完成的URL


    private static final String MSERVER_URL_HEAF = "http://";
    private static final String MSERVER_URL = "192.168.1.72";
    private static final String MSERVER_PORT = "8080";
    public static final String MSERVER_REQUEST = MSERVER_URL_HEAF + MSERVER_URL + ":" + MSERVER_PORT + "/DayWallpaper/";

    //接口实现===================
    public static final String MSERVER_LOGIN = "login?";


    //腾讯优图api  标签识别==========================================================================
    public static final String APP_ID = "10193152";
    public static final String SECRET_ID = "AKIDPccR2OgqKadS2OoLxPcZMvFKAlBMQ7g0";
    public static final String SECRET_KEY = "DCwXt30iBPP9i4C1gHemgU4ShhWDfroX";
    public static final String USER_ID = "1076409897";
    //腾讯优图api===================================================================================


    //腾讯Ai开放平台 官网：https://ai.qq.com/doc/home.shtml==========================================
    public final static String APP_KEY_TC = "2z0A37T4tAmhoHWE";
    public final static String APP_ID_TC = "2131063130";
    //看图说话
    public final static String IMAGETOTEXT = "https://api.ai.qq.com/fcgi-bin/vision/vision_imgtotext";
    //多标签识别
    public final static String IMAGETAB = "https://api.ai.qq.com/fcgi-bin/image/image_tag";
    //场景识别
    public final static String VISIONSCENER = "https://api.ai.qq.com/fcgi-bin/vision/vision_scener";
    //智能闲聊
    public final static String GETTEXTCHAT = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
    //腾讯Ai开放平台 官网：https://ai.qq.com/doc/home.shtml==========================================


    /**
     * 对话框标识
     */
    public static String DIALOG_L = "L";//加载
    public static String DIALOG_W = "W";//警告
    public static String DIALOG_E = "E";//错误
    public static String DIALOG_M = "M";//普通消息

    public static long server_time;
    public static String simTime;//20190101格式的时间

    public static class info {
        public static String enddate;
        public static String copyright;
        public static String tab;
        public static boolean isHate;
        public static int cloudVersionCode;
        public static int LocalVersionCode;
        public static int picTotal;
    }
}
