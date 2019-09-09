package com.xz.daywallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xz.com.log.LogUtil;

import java.io.File;

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

    public static void saveState(Context context, String key,boolean data){
        SharedPreferences sp = context.getSharedPreferences("app_state",
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, data).apply();
    }
    public static boolean getState(Context context, String key, boolean defValue){
        SharedPreferences sp = context.getSharedPreferences("app_state",
                Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    public static boolean deleteDate(Context context, String FilenName){
//        File file = new File(context.getFilesDir()+File.separator+"shared_prefs"+File.separator+FilenName+".xml");
        File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs", FilenName+".xml");

        //判断路径是否存在
        if (!file.exists()){
            LogUtil.d("a"+file.getAbsolutePath());
            return false;
        }
        //判断是文件夹还是文件
        if (file.isDirectory()){
            LogUtil.d("b");
            return false;
        }
        LogUtil.d("c");

        return file.delete();
    }

    public static void saveString(Context context, String file,String key,String data){
        SharedPreferences sp = context.getSharedPreferences(file,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, data).apply();
    }
    public static String getString(Context context, String file,String key, String defValue){
        SharedPreferences sp = context.getSharedPreferences(file,
                Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }

}
