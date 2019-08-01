package com.xz.com.log;

import android.content.Context;

import java.io.File;
import java.io.IOException;

/**
 * 日志工具初始化
 *
 * @author <a href="http://www.xzlyf.club/">xzlyf</a>
 * @version v0.01
 * @date 2019/7/31
 */
public class LogConfig {
    private static LogConfig logConfig;
    private static String flag = "default";//输出标识

    private LogConfig() {
    }

    /**
     * 获取单例类
     *
     * @return
     */
    public static LogConfig getInstance() {
        if (logConfig == null) {
            return new LogConfig();
        }
        return logConfig;
    }

    /**
     * 保存日志到本地
     * 注意这可能会影响性能
     * 路径：/storage/emulated/0/Android/data/publi.xz.com.smartcoupon/files/log
     *
     * @param vs 开关 控制输出
     */
    public void toLocal(Context context, boolean vs) {
        if (!vs) {
            LogUtil.showlog = false;//要使用该功能必须要打开showlog开关
            LogUtil.sv  = false;
            return;
        }
        //日志路径：/storage/emulated/0/Android/data/publi.xz.com.smartcoupon/files/log
        File file = context.getExternalFilesDir("log");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.d("日志地址：" + file.getAbsoluteFile());
        LogUtil.toLocal(file.getAbsoluteFile().toString());
    }

    public void setShowLog(boolean showLog) {
        LogUtil.showlog = showLog;
    }

    public static String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        LogConfig.flag = flag;
    }

}
