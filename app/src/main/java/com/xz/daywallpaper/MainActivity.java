package com.xz.daywallpaper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xz.com.log.LogConfig;
import com.xz.com.log.LogUtil;
import com.xz.daywallpaper.base.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView menu;
    private CardView infoView;
    private TextView copyright;
    private ImageView mainPic;




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
        menu.setOnClickListener(this);
        mainPic.setOnClickListener(this);
    }
    @Override
    public void showData(Object object) {

    }
    @Override
    public void init_Data() {
        init_log();
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
        switch (view.getId()){
            case R.id.menu:
                menu.startAnimation(xuanzhaun);
                break;
            case R.id.main_pic:
                infoView.startAnimation(weiyi);
                break;
        }
    }

}
