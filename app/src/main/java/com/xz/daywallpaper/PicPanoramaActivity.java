package com.xz.daywallpaper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;
import com.xz.daywallpaper.utils.ImageUtil;
import com.xz.daywallpaper.utils.TransparentBarUtil;

/**
 * 全景图片
 */
public class PicPanoramaActivity extends AppCompatActivity{
    private GyroscopeObserver gyroscopeObserver;

    @Override
    protected void onPause() {
        super.onPause();
        gyroscopeObserver.unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroscopeObserver.register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_panorama);
        //透明状态栏
        TransparentBarUtil.makeStatusBarTransparent(this);
        String path = getIntent().getStringExtra("pic_uri");


        gyroscopeObserver = new GyroscopeObserver();
        gyroscopeObserver.setMaxRotateRadian(Math.PI / 9);
        PanoramaImageView panoramaImageView = findViewById(R.id.panorama_image_view);
        panoramaImageView.setGyroscopeObserver(gyroscopeObserver);
        panoramaImageView.setImageBitmap(ImageUtil.lcoalImage2Bitmap(path));
    }

}
