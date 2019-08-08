package com.xz.daywallpaper.widget;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xz.daywallpaper.R;
import com.xz.daywallpaper.utils.ActivityUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static com.xz.daywallpaper.utils.NetUtil.isNetworkAvailable;
import static com.xz.daywallpaper.utils.SystemUtil.newInstallAppIntent;

/**
 * 更新对话框
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView versionName;
    private TextView versionMsg;
    private LinearLayout chooseFun;
    private Button atCancel;
    private Button atDownload;
    private LinearLayout chooseFun2;
    private ProgressBar progressBar;
    private TextView progressSpeed;
    private TextView progressPlan;
    private TextView file_lenght;
    private String link;
    private String DEFAULT_APK_PATH;
    private int level = 0;
    private String msg;
    private String name;


    public UpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater1 = LayoutInflater.from(mContext);
        View view1 = inflater1.inflate(R.layout.dialog_update, null);
        setContentView(view1);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);

        versionName = findViewById(R.id.version_name);
        versionMsg = findViewById(R.id.version_msg);
        chooseFun = findViewById(R.id.choose_fun);
        atCancel = findViewById(R.id.atCancel);
        atDownload = findViewById(R.id.atDownload);
        chooseFun2 = findViewById(R.id.choose_fun_2);
        progressBar = findViewById(R.id.progress_bar);
        progressSpeed = findViewById(R.id.progress__speed);
        progressPlan = findViewById(R.id.progress_plan);
        file_lenght = findViewById(R.id.file_lenght);
        atCancel.setOnClickListener(this);
        atDownload.setOnClickListener(this);

        DEFAULT_APK_PATH = mContext.getExternalFilesDir("apk") + "/update.apk";

    }

    @Override
    public void show() {
        super.show();

        if (level == 0) {

            versionMsg.setText(msg);
            versionName.setText("v" + name);
        } else if (level == 1) {
            versionName.setTextColor(Color.RED);
            versionName.setText("v" + name + '\b' + "(不可忽略此版本)");
            versionMsg.setText(msg);
            atCancel.setText("退出");
            //限制退出
            setCancelable(false);

        }
    }


    /**
     * 更新内容
     *
     * @param updateMsg
     */
    public void setMsg(String updateMsg) {
        this.msg = updateMsg;
    }


    /**
     * 版本名称
     *
     * @param Name
     */
    public void setVersionName(String Name) {
        this.name = Name;
    }

    /**
     * 下载地址
     *
     * @param link
     */
    public void setDownloadLink(String link) {
        this.link = link;
    }

    /**
     * 重要程度
     *
     * @param i
     */
    public void setLevel(int i) {
        level = i;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.atCancel:
                if (level == 0) {
                    dismiss();
                } else if (level == 1) {
                    ActivityUtil.finishAll();
                }
                break;
            case R.id.atDownload:
                chooseFun.setVisibility(View.GONE);
                chooseFun2.setVisibility(View.VISIBLE);
                Download download = new Download();
                download.execute(link);
                break;
        }
    }

    class Download extends AsyncTask<String, Integer, Boolean> {
        //创建时
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 销毁时
         *
         * @param aBoolean true 下载成功  false 下载失败
         */
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (level == 0) {
                installApk(aBoolean);
            } else if (level == 1) {
                installApk2();
            }

        }

        //更新ui
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            progressPlan.setText(values[0] + "%");
            file_lenght.setText(values[1] + "M");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            //检查网络
            if (!isNetworkAvailable(mContext)) {
                return false;
            }
            File filePath = new File(DEFAULT_APK_PATH);
            if (!filePath.getParentFile().exists()) {
                filePath.mkdir();
            }
            if (filePath.exists()) {
                filePath.delete();
            }

            InputStream inputStream = null;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filePath);
                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                //网速类
                inputStream = connection.getInputStream();
                //获取文件流大小
                long file_lenght = connection.getContentLength();
                int subFileLenght = (int) (file_lenght / 1024 / 1024);
                int len;
                int total_leng = 0;
                byte[] bytes = new byte[1024];
                while ((len = inputStream.read(bytes)) != -1) {
                    total_leng += len;
                    int value = (int) ((total_leng / (float) file_lenght) * 100);

                    //调用更新ui
                    publishProgress(value, subFileLenght);
                    fos.write(bytes, 0, len);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
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

            return true;
        }

        /**
         * 模式1，不强制安装
         */
        private void installApk(boolean aBoolean) {
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pi;
            String title = "";
            String content = "";
            //销毁对话框
            dismiss();
            //判断是否下载成功，设置对应的标识
            if (aBoolean) {
                Intent intent = newInstallAppIntent(mContext, new File(DEFAULT_APK_PATH));
                pi = PendingIntent.getActivity(mContext, 0, intent, 0);
                title = "下载成功";
                content = "点击开始安装吧~";
//                AppUtils.installApp(DEFAULT_APK_PATH);
            } else {
                title = "网络不稳定";
                content = "请稍后重试吧~";
                pi = null;
            }

            NotificationCompat.Builder builder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //如果是8以上的系统。需要传一个channelId.
                NotificationChannel channel = new NotificationChannel("chat", "123", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true); //是否在桌面icon右上角展示小红点 
                channel.setLightColor(Color.RED); //小红点颜色   
                channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知   
                notificationManager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(mContext, "chat");

                builder.setPriority(NotificationManager.IMPORTANCE_HIGH);//设置通知的优先级


            } else {
                builder = new NotificationCompat.Builder(mContext);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);//设置通知的优先级

            }

            builder
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.menu)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.drawable.menu))
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pi) // 设定点击通知之后启动的内容，这个内容由方法中的参数：PendingIntent对象决定
                    .setAutoCancel(true); // 设置点击通知之后通知是否消失
            builder.setVibrate(new long[]{1000, 500, 2000});
            Notification notification = builder.build(); // 创建通知（每个通知必须要调用这个方法来创建）
            notificationManager.notify(1, notification);
        }

        /**
         * 模式2 ：强制安装，下载后直接打开安装器，然后推出程序
         */
        private void installApk2(){
            Intent intent = newInstallAppIntent(mContext, new File(DEFAULT_APK_PATH));
            mContext.startActivity(intent);
            ActivityUtil.finishAll();

        }
    }
}
