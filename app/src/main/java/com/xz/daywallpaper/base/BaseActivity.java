package com.xz.daywallpaper.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xz.com.log.LogUtil;
import com.xz.daywallpaper.R;
import com.xz.daywallpaper.presenter.Presenter;
import com.xz.daywallpaper.utils.TransparentBottom;


public abstract class BaseActivity extends AppCompatActivity {
    private Context mContext;
    public Presenter presenter;
    //标识
    private final int dataCallback = 1002;
    private final int toast_bs = 1003;
    private final int show_d = 1004;
    private final int hide_d = 1005;
    private final int show_l = 1006;
    private final int hide_l = 1007;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //showData()回调
                case dataCallback:
                    showData(msg.obj);
                    break;
                case toast_bs:
                    mToast_handler(msg.obj.toString());
                    break;
                case show_d:
                    String[] t = (String[]) msg.obj;
                    show_d_handler(t[0], t[1]);
                    break;
                case hide_d:
                    dismiss_d_handler();
                    break;
                case show_l:
                    show_load_handler();
                    break;
                case hide_l:
                    dismiss_l_handler();
                    break;

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LogUtil.toSave();
    }


    /**
     * 异步线程调用
     * 把数据回到主线
     * 回到主线程封装
     */
    public void backToUi(Object object) {
        //通过handler.obtainMessage()可以减少内存的使用
        Message msg = handler.obtainMessage();
        msg.what = dataCallback;
        msg.obj = object;
        handler.sendMessage(msg);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //公共属性
        mContext = this;

        //公共方法
        setContentView(getLayoutResource());
        findID();
        getPermission();


    }


    /**
     * 获取权限
     */
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                init();
            }
        } else {
            init();
        }
    }

    private void init() {
        presenter = new Presenter(this);
        init_Data();
        //创建一个HomeUp按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //透明状态栏
//        TransparentBottom.transparentBottom(this);
    }





    /**
     * homeUp按钮
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 权限结果回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                    init();
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public abstract int getLayoutResource();

    public abstract void findID();

    public abstract void init_Data();

    public abstract void showData(Object object);


    //常用方法
    Toast mToast;
    private BaseDialog mDialog;
    private ProgressDialog progressDialog;

    /**
     * 子类调用
     * 常用方法
     *
     * @param text
     */
    public void mToast(String text) {
        Message msg = handler.obtainMessage();
        msg.what = toast_bs;
        msg.obj = text;
        handler.sendMessage(msg);
    }

    /**
     * 子类调用
     * 显示对话框
     *
     * @param text 消息
     * @param type 类型
     */
    public void showDialog(final String text, final String type) {
        Message msg = handler.obtainMessage();
        msg.what = show_d;
        msg.obj = new String[]{text, type};
        handler.sendMessage(msg);
    }

    /**
     * 子类调用
     * 销毁对话框
     */
    public void dismissDialog() {
        Message msg = handler.obtainMessage();
        msg.what = hide_d;
        handler.sendMessage(msg);
    }

    /**
     * 子类调用
     * 显示加载框
     */
    public void showLoading() {
        Message msg = handler.obtainMessage();
        msg.what = show_l;
        handler.sendMessage(msg);
    }

    /**
     * 子类调用
     * 销毁加载框
     */
    public void dismissLoading() {
        Message msg = handler.obtainMessage();
        msg.what = hide_l;
        handler.sendMessage(msg);
    }

    /**
     * Handler执行
     * 回到主线
     *
     * @param text
     */
    private void mToast_handler(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    /**
     * Handler执行
     * 回到主线程
     *
     * @param msg
     * @param type
     */
    private void show_d_handler(String msg, String type) {
        mDialog = new BaseDialog(mContext, R.style.base_dialog);
        mDialog.create();
        mDialog.setType(type);
        mDialog.setMsg(msg);
        mDialog.show();
    }

    /**
     * Handler执行
     * 回到主线程
     */
    private void dismiss_d_handler() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * Handle调用
     * 显示加载框
     */
    private void show_load_handler() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("加载中");
        progressDialog.setMessage("稍等片刻!");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Handler调用
     * 销毁加载框
     */
    private void dismiss_l_handler() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
