package com.xz.daywallpaper.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理工具类
 * 支持一键移除活动
 */
public class ActivityUtil {

    public static List<Activity> list = new ArrayList<>();

    public static void addActivity(Activity activity){
        list.add(activity);
    }
    public static void removeActivity(Activity activity){
        list.remove(activity);
    }
    public static void finishAll(){
        for (Activity ac:list){
            if (!ac.isFinishing()){
                ac.finish();
            }
        }
        list.clear();
    }
}
