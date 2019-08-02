package com.xz.daywallpaper.presenter;

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

public class Presenter {
    private Model model;
    private BaseActivity view;

    public Presenter(BaseActivity view) {
        this.view = view;
        model = new Model();
    }

    /**
     * 初始化主图
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
                //连接问题
                view.mToast("当前网络异常x0");
                view.dismissLoading();
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
            Local.info.copyright =SharedPreferencesUtil.getPicData(view,Local.simTime,"copyright","null");
            Local.info.enddate = SharedPreferencesUtil.getPicData(view,Local.simTime,"enddate","null");
            view.backToUi(Local.picTDir);
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
                    SharedPreferencesUtil.savePicData(view,Local.simTime,"enddate",pic.getEnddate());
                    SharedPreferencesUtil.savePicData(view,Local.simTime,"copyright",pic.getCopyright());
                    SharedPreferencesUtil.savePicData(view,Local.simTime,"url",Local.BING_HEAD+pic.getUrl());

                    downloadPic(pic.getUrl());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception e) {
                view.mToast("当前网络异常x1");
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


}
