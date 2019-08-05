package com.xz.daywallpaper.utils;

import java.text.SimpleDateFormat;


public class Date {
    /**
     * 返回当前系统时间
     * yyyyMMddHHmmss
     */
    public static String getSimDate(){
        return new SimpleDateFormat(" yyyyMMdd").format(new java.util.Date());
    }

    /**
     *
     * @return
     */
    public static String getSimDate(Long time){
        return new SimpleDateFormat(" yyyyMMdd").format(new java.util.Date(time));
    }
}
