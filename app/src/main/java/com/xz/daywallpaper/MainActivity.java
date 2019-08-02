package com.xz.daywallpaper;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.com.log.LogConfig;
import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.custom.MenuDialog;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView menu;
    private CardView infoView;
    private CardView headview;
    private TextView enddate;
    private TextView copyright;
    private ImageView mainPic;
    private boolean isClick = false;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void findID() {
        menu = findViewById(R.id.menu);
        infoView = findViewById(R.id.info_view);
        copyright = findViewById(R.id.copyright);
        mainPic = findViewById(R.id.main_pic);
        headview = findViewById(R.id.head_view);
        enddate = findViewById(R.id.enddate);
        menu.setOnClickListener(this);
        mainPic.setOnClickListener(this);
    }

    @Override
    public void showData(Object object) {

    }

    @Override
    public void init_Data() {
        init_log();
        init_anim();
        showPicInfo();

    }

    /**
     * 初始化日志工具
     */
    private void init_log() {
        LogConfig config = LogConfig.getInstance();
        config.setShowLog(true);
        config.setFlag("xzlyf");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                menu.startAnimation(xuanzhaun);
                showMenu();
                break;
            case R.id.main_pic:

                if (isClick) {
                    hidePicInfo();
                } else {
                    showPicInfo();
                }
                break;
        }
    }


    private MenuDialog menuDialog;
    /**
     * 菜单对话框
     */
    private void showMenu() {
        menuDialog = new MenuDialog(this, R.style.base_dialog);
        menuDialog.create();
        menuDialog.show();
    }

    private void showPicInfo() {
        infoView.startAnimation(weiyi);
        headview.startAnimation(weiyi2);
        isClick = true;
    }

    private void hidePicInfo() {
        infoView.startAnimation(weiyi_Res);
        headview.startAnimation(weiyi2_Res);
        isClick = false;
    }

    /**
     * 动画
     */
    public Animation xuanzhaun;
    public Animation weiyi;
    public Animation weiyi_Res;
    public Animation weiyi2;
    public Animation weiyi2_Res;

    private void init_anim() {
        xuanzhaun = AnimationUtils.loadAnimation(this, R.anim.xuanzhuan);
        weiyi = AnimationUtils.loadAnimation(this, R.anim.weiyi);
        weiyi.setFillAfter(true);//使控件停留在播放动画后的位置
        weiyi_Res = AnimationUtils.loadAnimation(this, R.anim.weiyi_reversal);
        weiyi_Res.setFillAfter(true);//
        weiyi2 = AnimationUtils.loadAnimation(this, R.anim.weiyi_2);
        weiyi2.setFillAfter(true);//
        weiyi2_Res = AnimationUtils.loadAnimation(this, R.anim.weiyi_2_reversal);
        weiyi2_Res.setFillAfter(true);//
    }


}
