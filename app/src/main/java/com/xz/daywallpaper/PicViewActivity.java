package com.xz.daywallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.xz.daywallpaper.utils.TransparentBarUtil;

public class PicViewActivity extends AppCompatActivity implements View.OnClickListener {
    private PhotoView mPhotoView;
    private Button btn1;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picview);

        //透明状态栏
        TransparentBarUtil.makeStatusBarTransparent(this);

        path = getIntent().getStringExtra("pic_uri");

        btn1 = findViewById(R.id.Panorama_btn);
        btn1.setOnClickListener(this);
        mPhotoView = findViewById(R.id.photo_view);
        // 启用图片缩放功能
        mPhotoView.enable();
        mPhotoView.setMaxScale(4.0f);
        Glide.with(this).load(path).into(mPhotoView);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Panorama_btn:
                startActivity(new Intent(this, PicPanoramaActivity.class).putExtra("pic_uri", path));
                break;
        }
    }
}
