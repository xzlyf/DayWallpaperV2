package com.xz.daywallpaper.utils;

import java.io.File;

public class FileUtil {
    public static boolean deleteFile(String path){
        File file = new File(path);
        //判断路径是否存在
        if (!file.exists()){
            return false;
        }
        //判断是文件夹还是文件
        if (file.isDirectory()){
            return false;
        }

        return file.delete();

    }
}
