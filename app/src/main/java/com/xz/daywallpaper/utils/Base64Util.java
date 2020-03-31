package com.xz.daywallpaper.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

/**
 * @author czr
 * @date 2020/3/27
 */
public class Base64Util {
    /**
     * 将文件转成base64 字符串
     *
     * @param path 文件路径
     * @return *
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new String(Base64.encode(buffer, Base64.DEFAULT), Charset.forName("UTF-8"));
    }

    /**
     * 将base64字符解码保存文件
     * @param base64Code base64源数据
     * @param targetPath 保存文件位置
     */
    public static void decoderBase64File(String base64Code, String targetPath)
            throws Exception {
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

}
