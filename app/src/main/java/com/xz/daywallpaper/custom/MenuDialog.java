package com.xz.daywallpaper.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.xz.com.log.LogUtil;
import com.xz.daywallpaper.R;

public class MenuDialog extends Dialog implements View.OnClickListener{
    private final Context mContext;
    private CardView v1;
    private CardView v2;
    private CardView v3;
    private CardView v4;
    //播放完最后一个动画的标识
    private boolean isOver = false;

    public MenuDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.dialog_menu, null));
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.LEFT);//底部显示
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.6); // 高度设置为屏幕的0.6
        lp.dimAmount = 0.2f;//背景不变暗
        dialogWindow.setAttributes(lp);

        findId();
        init_anim();
        showItem();

    }
    @Override
    public void onClick(View view) {
        dismiss();
        switch (view.getId()){
            case R.id.v1:
                break;
        }
    }

    @Override
    public void dismiss() {
        if (!isOver) {
            hideItem();
            return;
        } else {
            super.dismiss();
        }


    }

    private void findId() {
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);
    }

    /**
     * 进入动画
     */
    private void showItem() {
        v1.startAnimation(showItem);
        v2.startAnimation(showItem2);
        v3.startAnimation(showItem3);
        v4.startAnimation(showItem4);
    }

    /**
     * 退出动画
     */
    private void hideItem() {
        v4.startAnimation(hideItem4);
        v3.startAnimation(hideItem3);
        v2.startAnimation(hideItem2);
        v1.startAnimation(hideItem);
    }


    /**
     * 动画
     */
    private Animation showItem;
    private Animation showItem2;
    private Animation showItem3;
    private Animation showItem4;
    private Animation hideItem;
    private Animation hideItem2;
    private Animation hideItem3;
    private Animation hideItem4;

    private void init_anim() {
        showItem = AnimationUtils.loadAnimation(mContext, R.anim.translate_item_left);
        showItem2 = AnimationUtils.loadAnimation(mContext, R.anim.translate_item_left);
        showItem3 = AnimationUtils.loadAnimation(mContext, R.anim.translate_item_left);
        showItem4 = AnimationUtils.loadAnimation(mContext, R.anim.translate_item_left);
        showItem2.setStartOffset(100);//延时
        showItem3.setStartOffset(200);
        showItem4.setStartOffset(300);
        hideItem = AnimationUtils.loadAnimation(mContext, R.anim.translate_item_right);
        hideItem2 = AnimationUtils.loadAnimation(mContext, R.anim.translate_item_right);
        hideItem3 = AnimationUtils.loadAnimation(mContext, R.anim.translate_item_right);
        hideItem4 = AnimationUtils.loadAnimation(mContext, R.anim.translate_item_right);
        hideItem3.setStartOffset(100);
        hideItem2.setStartOffset(200);
        hideItem.setStartOffset(300);
        hideItem4.setFillAfter(true);
        hideItem3.setFillAfter(true);
        hideItem2.setFillAfter(true);
        hideItem.setFillAfter(true);

        hideItem.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isOver = true;
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}