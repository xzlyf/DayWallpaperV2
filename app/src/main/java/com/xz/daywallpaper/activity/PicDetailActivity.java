package com.xz.daywallpaper.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xz.daywallpaper.R;
import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.utils.Date;

import java.io.File;

public class PicDetailActivity extends BaseActivity implements View.OnClickListener {
    private String path;
    private ImageView mMainPic;
    private TextView infoPic;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_pic_detail;
    }

    @Override
    public void findID() {
        mMainPic = findViewById(R.id.main_pic);
        infoPic = findViewById(R.id.info_pic);

    }

    @Override
    public void init_Data() {
        path = getIntent().getStringExtra("pic_uri");

        Glide.with(this).load(path).into(mMainPic);

        File file = new File(path);
        infoPic.setText("创建时间："+Date.getSimDate(file.lastModified(), "yyyy年mm月dd日 HH:mm:ss")+"");

    }

    @Override
    public void showData(Object object) {

    }

    @Override
    public void onClick(View view) {

    }
}
