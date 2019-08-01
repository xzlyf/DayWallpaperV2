package com.xz.com.log;

import android.support.annotation.NonNull;
import android.util.Log;

import com.xz.com.log.utils.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 日志工具调用类
 *
 * @author <a href="http://www.xzlyf.club/">xzlyf</a>
 * @version v0.01
 * @date 2019/7/31
 */
public class LogUtil {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    protected static boolean showlog =false;//默认关闭
    private static String tag = LogConfig.getFlag();
    private static StringBuilder builder;
    protected static boolean sv = false;//日志保存至本地开关
    private static BufferedWriter bufferedWriter;
    private static FileWriter fileWriter;
    private static String pa;

    /**
     * 保存日志到本地
     * 保存原理：当缓存日志接近缓存大小时，自动向本地输出。
     * 随后清空缓存大小，然后重复刚才的步骤
     *
     * @param path 日志地址
     */
    protected static void toLocal(String path) {
        builder = new StringBuilder();
        pa = path + File.separator + Date.getSimDate() + ".txt";
    }

    /**
     * 自动保存
     * 当缓存大于n时自动写入磁盘
     *
     * @param log
     */
    private static void autoSave(String log) {
        if (!sv) return;
        builder.append(Date.getDate() + ":");
        builder.append(log);
        builder.append("\n");
        if (builder.length() >= 32768) {
            toSave();
        }

    }

    /**
     * 可手动保存
     * 退出程序前手动保存
     */
    public static void toSave() {
        if (!sv) return;
        try {
            fileWriter = new FileWriter(pa, true);
            bufferedWriter = new BufferedWriter(fileWriter);//true开启追加数据
            bufferedWriter.write(builder.toString());
            bufferedWriter.flush();
            builder.delete(0, builder.length());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
                bufferedWriter.close();
                fileWriter = null;
                bufferedWriter = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * json格式输出=================================================================
     *
     * @param head 标识
     * @param msg  json内容
     */
    public static void json(String head, String msg) {
        if (!showlog) return;

        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        printLinetop(Log.DEBUG);

        Log.d(tag, "║ " + getFunctionName());

        printLinemiddle(Log.DEBUG);

        Log.d(tag, "║ " + head);

        printLinemiddle(Log.DEBUG);

//        message = head + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        printLinebottom(Log.DEBUG);
    }

    /**
     * list集合输出=================================================================
     *
     * @param list
     */
    public static void list(String head, @NonNull List<?> list) {
        printLinetop(Log.DEBUG);
        Log.println(Log.DEBUG, tag, "║ " + getFunctionName());
        printLinemiddle(Log.DEBUG);
        Log.d(tag, "║ " + head);
        printLinemiddle(Log.DEBUG);
        for (Object obj : list) {
            Log.println(Log.DEBUG, tag, "║ " + obj.toString());
        }
        printLinebottom(Log.DEBUG);
    }

    /**
     * map集合的遍历================================================================
     *
     * @param map
     */
    public static void map(String head, @NonNull Map<?, ?> map) {
        printLinetop(Log.DEBUG);
        Log.println(Log.DEBUG, tag, "║ " + getFunctionName());
        printLinemiddle(Log.DEBUG);
        Log.d(tag, "║ " + head);
        printLinemiddle(Log.DEBUG);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Log.println(Log.DEBUG, tag, "║ " + entry.getKey() + ":" + entry.getValue());
        }
        printLinebottom(Log.DEBUG);
    }


    /**
     * 普通日志输出==================================================================
     *
     * @param obj
     */
    public static void v(Object... obj) {
        print(Log.VERBOSE, obj);
    }

    public static void d(Object... obj) {
        print(Log.DEBUG, obj);
    }

    public static void i(Object... obj) {
        print(Log.INFO, obj);
    }

    public static void w(Object... obj) {
        print(Log.WARN, obj);
    }

    public static void e(Object... obj) {
        print(Log.ERROR, obj);
    }


    private static void print(int verbose, Object[] obj) {

        if (!showlog) return;
        printLinetop(verbose);
        Log.println(verbose, tag, "║ " + getFunctionName());
        printLinemiddle(verbose);
        for (Object line : obj) {
            Log.println(verbose, tag, "║ " + line);
            autoSave(line.toString());
        }
        printLinebottom(verbose);


    }

    private static void printLinetop(int level) {
        Log.println(level, tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
    }

    private static void printLinemiddle(int level) {
        Log.println(level, tag, "╠═══════════════════════════════════════════════════════════════════════════════════════");

    }

    private static void printLinebottom(int level) {
        Log.println(level, tag, "╚══════════════════════════════════════════════════════════════════════════════════════");

    }


    /**
     * 获取打印信息所在方法名，行号等信息
     *
     * @return
     */
    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts != null) {
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals(Thread.class.getName())) {
                    continue;
                }
                if (st.getClassName().equals(LogUtil.class.getName())) {
                    continue;
                }
                return "Thread:" + Thread.currentThread().getName() + ", at " + st.getClassName() + "." + st.getMethodName()
                        + "(" + st.getFileName() + ":" + st.getLineNumber() + ")";
            }
        }
        return null;
    }
}
