package com.xz.com.log.utils;

import java.text.SimpleDateFormat;


public class Date {
    /**
     * 返回当前时间
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getDate(){
        return new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }
    /**
     * 返回当前时间
     * yyyyMMddHHmmss
     */
    public static String getSimDate(){
        return new SimpleDateFormat(" yyyyMMddHHmmss").format(new java.util.Date());
    }
}
