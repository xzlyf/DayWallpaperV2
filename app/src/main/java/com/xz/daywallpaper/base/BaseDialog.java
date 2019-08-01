package com.xz.daywallpaper.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xz.daywallpaper.R;
import com.xz.daywallpaper.constant.Local;

public class BaseDialog extends Dialog {
    private Context mContext;
    private TextView title;
    private TextView msg;
    private ImageView type;
    private LinearLayout bg;

    public BaseDialog(@NonNull Context context) {
        super(context);
        mContext = context;

    }


    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

    }
    public void setMsg(String msg){
        this.msg.setText(msg);
    }
    @Override
    public void dismiss() {
        super.dismiss();
        title = null;
        msg = null;
        type = null;
        bg = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater1 = LayoutInflater.from(mContext);
        View view1 = inflater1.inflate(R.layout.dialog_base, null);
        setContentView(view1);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        title = findViewById(R.id.dialog_base_title);
        msg = findViewById(R.id.dialog_base_msg);
        type = findViewById(R.id.type_icon);
        bg = findViewById(R.id.title_background);
    }

    public void setType(String type) {
        if (type.equals(Local.DIALOG_L)){
            //加载
            loading();
        }else if (type.equals(Local.DIALOG_E)){
            //错误
            error();
        }else if (type.equals(Local.DIALOG_M)){
            //普通消息
            tips();
        }else if (type.equals(Local.DIALOG_W)){
            //警告
            warning();
        }else{
            //未知
        }
    }

    private void loading() {
        title.setText("加载中");
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        bg.setBackgroundColor(Color.parseColor("#2ecc71"));
    }

    private void tips() {
        title.setText("提示");
        bg.setBackgroundColor(Color.parseColor("#e67e22"));

    }

    private void warning() {
        title.setText("异常");
        bg.setBackgroundColor(Color.parseColor("#f1c40f"));
    }

    private void error() {
        title.setText("错误");
        bg.setBackgroundColor(Color.parseColor("#e74c3c"));
    }
}
