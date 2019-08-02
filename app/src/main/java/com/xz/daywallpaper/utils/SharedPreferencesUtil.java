package com.xz.daywallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SP工具类
 */
public class SharedPreferencesUtil {

    /*
    apply() 异步提交
    commit() 同步提交，效率会比apply异步提交的速度慢，但是apply没有返回值，永远无法知道存储是否失败

     */


    /**
     * 固定式存储
     * @param context
     * @param key
     * @param data
     */
    public static void savePicData(Context context,String fileName, String key,String data){
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, data).apply();
    }

    /**
     * 固定式读取
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getPicData(Context context,String fileName, String key, String defValue){
        SharedPreferences sp = context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }


}
