package com.xz.daywallpaper.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.xz.com.log.LogUtil;
import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.constant.Local;
import com.xz.daywallpaper.entity.PIc;
import com.xz.daywallpaper.model.IModel;
import com.xz.daywallpaper.model.Model;
import com.xz.daywallpaper.utils.Date;
import com.xz.daywallpaper.utils.NetUtil;
import com.xz.daywallpaper.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Presenter {
    private Model model;
    private BaseActivity view;

    public Presenter(BaseActivity view) {
        this.view = view;
        model = new Model();
    }

    /**
     * 初始化主图====================================================================================
     * 图片信息和图片源文件两者不可缺一，缺了话重新缓存，缓存失败就提示失败
     */
    public void initMainPic() {
        view.showLoading();
        //1.获取服务器时间
        model.getDataFromNet(Local.GET_SERVER_TIME, new IModel.OnLoadCompleteListener() {
            @Override
            public void success(String data) {
                JSONObject obj = null;

                try {
                    //    LogUtil.w("网络时间："+data);
                    obj = new JSONObject(data);
                    if (obj.getString("code").equals("1")) {
                        JSONObject obj2 = obj.getJSONObject("data");
                        if (obj2 != null) {

                            Local.server_time = obj2.getLong("time");
                            Local.simTime = Date.getSimDate(Local.server_time);

                            checkLoclPic(Local.simTime);
                        }


                    } else if (obj.getString("code").equals("0")) {
                        //获取服务器时间失败
                        //使用本地时间
                        Local.simTime = Date.getSimDate();
                        checkLoclPic(Local.simTime);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    checkLoclPic(Date.getSimDate());
                }

            }

            @Override
            public void failed(Exception e) {
                //使用本地时间
                Local.simTime = Date.getSimDate();
                checkLoclPic(Local.simTime);
//                view.mToast("当前网络异常x0");
            }
        });

    }

    /**
     * 检查本地是否存在当天的Pic
     *
     * @param today
     */
    private void checkLoclPic(String today) {

        //拼接地址
        Local.picDir = view.getExternalFilesDir("img").toString();
        Local.picTDir = Local.picDir + File.separator + today + ".jpg";
        File file = new File(Local.picTDir);
        if (file.exists()) {
            //存在
            //读取本地照片信息
            Local.info.copyright = SharedPreferencesUtil.getPicData(view, Local.simTime, "copyright", "null");
            Local.info.enddate = SharedPreferencesUtil.getPicData(view, Local.simTime, "enddate", "null");
            //如果两者有一数据找不到就重新加载网络的
            if (Local.info.copyright.equals("null") || Local.info.enddate.equals("null")) {
                getNetPic();
            } else {
                view.backToUi(Local.picTDir);
            }
        } else {
            //不存在
            getNetPic();
        }

    }

    /**
     * 加载网络Pic数据
     */
    private void getNetPic() {
        model.getDataFromNet(Local.BING_API, new IModel.OnLoadCompleteListener() {
            @Override
            public void success(String data) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(data);
                    JSONArray array = obj.getJSONArray("images");
                    Gson gson = new Gson();
                    PIc pic = gson.fromJson(array.get(0).toString(), PIc.class);

                    Local.info.copyright = pic.getCopyright();
                    Local.info.enddate = pic.getEnddate();
                    //保存Pic数据到本地
                    SharedPreferencesUtil.savePicData(view, Local.simTime, "enddate", pic.getEnddate());
                    SharedPreferencesUtil.savePicData(view, Local.simTime, "copyright", pic.getCopyright());
                    SharedPreferencesUtil.savePicData(view, Local.simTime, "url", Local.BING_HEAD + pic.getUrl());

                    downloadPic(pic.getUrl());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception e) {
                view.mToast("当前网络异常x1");
                view.backToUi(false);
                view.dismissLoading();
            }
        });
    }

    /**
     * 缓存到本地
     */
    private void downloadPic(String inurl) {
        //拼接url
        Local.BASE_URL = Local.BING_HEAD + inurl;
        File file = new File(Local.picTDir);

        InputStream inputStream = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            URL url = new URL(Local.BASE_URL);
            URLConnection connection = url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();

            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        view.backToUi(file.getAbsoluteFile());
    }

    /**
     * 查找本地缓存图片===============================================================================
     */
    public void findLocalPic() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                File[] list = new File(Local.picDir).listFiles();
                if (list.length==0){
                    view.backToUi(false);
                }

                List<File> fileList = new ArrayList<File>();
                for (File f : list) {
                    fileList.add(f);
                }
                //以文件名排序，这样就选修改了文件的日期本地缓存图片排序也没有影响--获取排序后的集合
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory() && o2.isFile())
                            return -1;
                        if (o1.isFile() && o2.isDirectory())
                            return 1;
                        return o2.getName().compareTo(o1.getName());
                    }
                });

                List<PIc> picList = new ArrayList<>();
                for (File f: fileList){
                    String today = f.getName().split(".jpg")[0];
                    PIc p= new PIc();
                    p.setUrl(f.getAbsolutePath());
                    p.setEnddate(SharedPreferencesUtil.getPicData(view,today,"enddate","null"));
                    p.setCopyright(SharedPreferencesUtil.getPicData(view,today,"copyright","null"));
                    picList.add(p);


                }

                view.backToUi(picList);

            }
        }).start();

    }
}
