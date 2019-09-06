package com.xz.daywallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.xz.daywallpaper.utils.ImageUtil;
import com.xz.daywallpaper.utils.TransparentBarUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PicViewActivity extends AppCompatActivity implements View.OnClickListener {
    private PhotoView mPhotoView;
    private Button btn1, btn2;
    private String path;
    private ImageView back;
    private LinearLayout menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picview);

        //透明状态栏
        TransparentBarUtil.makeStatusBarTransparent(this);

        path = getIntent().getStringExtra("pic_uri");

        btn1 = findViewById(R.id.Panorama_btn);
        btn2 = findViewById(R.id.wallpaper_btn);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        mPhotoView = findViewById(R.id.photo_view);
        back = findViewById(R.id.tv_back);
        menu = findViewById(R.id.tv_menu);
        back.setOnClickListener(this);
        // 启用图片缩放功能
        mPhotoView.enable();
        mPhotoView.setMaxScale(4.0f);
        Glide.with(this).load(path).into(mPhotoView);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Panorama_btn:
                startActivity(new Intent(this, PicPanoramaActivity.class).putExtra("pic_uri", path));
                break;
            case R.id.wallpaper_btn:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    startActivityForResult(WallpaperManager.getInstance(getApplicationContext())
                            .getCropAndSetWallpaperIntent(FileProvider.getUriForFile(this, "com.xz.daywallpaper", new File(path))),160);
                } else {
                    try {
                        WallpaperManager.getInstance(getApplicationContext()).setBitmap(ImageUtil.lcoalImage2Bitmap(path));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==160){
            if (resultCode==-1){
                Toast.makeText(this, "壁纸设置成功", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
