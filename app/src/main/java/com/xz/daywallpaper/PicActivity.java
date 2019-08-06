package com.xz.daywallpaper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.xz.daywallpaper.utils.TransparentBarUtil;

public class PicActivity extends AppCompatActivity {
    private PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        //透明状态栏
        TransparentBarUtil.makeStatusBarTransparent(this);

        String path = getIntent().getStringExtra("pic_uri");

        mPhotoView = findViewById(R.id.photo_view);
        // 启用图片缩放功能
        mPhotoView.enable();
        mPhotoView.setMaxScale(4.0f);
        Glide.with(this).load(path).into(mPhotoView);

    }
}
