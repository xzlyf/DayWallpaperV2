package com.xz.daywallpaper.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.google.gson.Gson;
import com.xz.com.log.LogUtil;
import com.xz.daywallpaper.R;
import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.constant.Local;
import com.xz.daywallpaper.entity.PIc;
import com.xz.daywallpaper.entity.PicTab;
import com.xz.daywallpaper.entity.Update;
import com.xz.daywallpaper.model.IModel;
import com.xz.daywallpaper.model.Model;
import com.xz.daywallpaper.utils.Date;
import com.xz.daywallpaper.utils.NetUtil;
import com.xz.daywallpaper.utils.SharedPreferencesUtil;
import com.youtu.Youtu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.logging.Logger;

import static com.xz.daywallpaper.constant.Local.APP_ID;
import static com.xz.daywallpaper.constant.Local.SECRET_ID;
import static com.xz.daywallpaper.constant.Local.SECRET_KEY;
import static com.xz.daywallpaper.constant.Local.USER_ID;

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
            Local.info.tab = SharedPreferencesUtil.getPicData(view, Local.simTime, "tab", "null");
            //如果两者有一数据找不到就重新加载网络的
            if (Local.info.copyright.equals("null") || Local.info.enddate.equals("null")) {
                getNetPic();
            } else {
                view.backToUi(Local.picTDir);
            }
            //标签数据信息判断
            if (Local.info.tab.equals("null")) {
                getNetPic();
            } else {
                //解析json
                JSONArray array = null;
                try {
                    array = new JSONArray(Local.info.tab);
                    List<PicTab> list = new ArrayList<>();
                    Gson gson = new Gson();

                    for (int i = 0; i < array.length(); i++) {
                        list.add(gson.fromJson(array.get(i).toString(), PicTab.class));
                    }
                    view.backToUi(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                    getNetPic();
                    view.mToast("标签内容被篡改!");
                }

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
                    view.mToast("解析异常");
                    view.backToUi(false);
                    view.dismissLoading();
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
            view.mToast("图片缓存失败");
            view.backToUi(false);
            view.dismissLoading();

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
        getPicTab();
    }

    /**
     * 腾讯优图api
     * 识别场景标签
     */
    private void getPicTab() {
        Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY, Youtu.API_YOUTU_END_POINT, USER_ID);
        JSONObject obj = null;
        try {
//            LogUtil.json("标签", obj.toString());
            obj = faceYoutu.ImageTagUrl(Local.BASE_URL);
            if (obj.getInt("errorcode") == 0) {

                JSONArray array = obj.getJSONArray("tags");
                List<PicTab> list = new ArrayList<>();
                Gson gson = new Gson();

                for (int i = 0; i < array.length(); i++) {
                    list.add(gson.fromJson(array.get(i).toString(), PicTab.class));
                }
                //保存标签数据到本地
                SharedPreferencesUtil.savePicData(view, Local.simTime, "tab", array.toString());
                view.backToUi(list);

//                Gson gson = new Gson();
//                view.backToUi(gson.fromJson(obj.toString(),PicTab.class));
            } else {
                view.mToast("AI场景标签识别错误" + obj.getString("errormsg"));
                view.dismissLoading();
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.mToast("标签获取失败");
            view.dismissLoading();
        }
    }

    /**
     * 对Pic进行模糊=================================================================================
     *
     * @param uri
     */
    public void blurryPic(final String uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(Local.picTDir);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //获取bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                if (bitmap == null) {
                    view.mToast("错误0x1");
                    return;
                }

                view.backToUi(blurBitmap(view, bitmap, 25f));
            }
        }).start();
    }

    //图片缩放比例
    private static final float BITMAP_SCALE = 0.15f;

    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    private static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    /**
     * 查找本地缓存图片===============================================================================
     */
    public void findLocalPic() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                File[] list = new File(Local.picDir).listFiles();
                if (list.length == 0) {
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
                for (File f : fileList) {
                    String today = f.getName().split(".jpg")[0];
                    PIc p = new PIc();
                    p.setUrl(f.getAbsolutePath());
                    p.setEnddate(SharedPreferencesUtil.getPicData(view, today, "enddate", "null"));
                    p.setCopyright(SharedPreferencesUtil.getPicData(view, today, "copyright", "null"));
                    picList.add(p);


                }

                view.backToUi(picList);

            }
        }).start();

    }

    /**
     * 检查更新操作==================================================================================
     */
    public void checkUpdate() {
        model.getDataFromNet(Local.UPDATE_SERVER, new IModel.OnLoadCompleteListener() {
            @Override
            public void success(String data) {
                JSONObject obj = null;

                try {
//                    LogUtil.json("更新数据",data);
                    obj = new JSONObject(data);
                    if (obj.getInt("value") == 1) {
                        JSONObject obj2 = obj.getJSONObject("data");
                        Gson gson = new Gson();
                        Update update = gson.fromJson(obj2.toString(), Update.class);
                        Local.info.cloudVersionCode = update.getCode();
                        //判断比较是否需要升级

                        if (Local.info.LocalVersionCode < Local.info.cloudVersionCode) {
                            view.backToUi(update);
                        }
                    } else {
                        //服务器问题
                        view.showDialog("服务器异常0x1",Local.DIALOG_E);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //解析问题-可能是网站404了
                    view.showDialog("服务器异常0x2",Local.DIALOG_E);


                }

            }

            @Override
            public void failed(Exception e) {

            }
        });
    }
}
