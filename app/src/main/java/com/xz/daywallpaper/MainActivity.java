package com.xz.daywallpaper;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xz.com.log.LogConfig;
import com.xz.com.log.LogUtil;
import com.xz.daywallpaper.adapter.TabAdapter;
import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.constant.Local;
import com.xz.daywallpaper.custom.MenuDialog;
import com.xz.daywallpaper.entity.PicTab;
import com.xz.daywallpaper.utils.SpacesItemDecorationVertical;

import java.util.List;

import static com.xz.daywallpaper.R.drawable.error;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView menu;
    private CardView infoView;
    private CardView headview;
    private TextView enddate;
    private TextView copyright;
    private ImageView mainPic;
    private RecyclerView recycler;
    private TabAdapter adapter;
    private boolean isClick = false;
    private NestedScrollView scrollView;


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
        recycler = findViewById(R.id.recycler_tab);
        scrollView = findViewById(R.id.scroll_bar);
        //长按查看图片
        mainPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(MainActivity.this,PicActivity.class).putExtra("pic_uri",Local.picTDir));
                return true;
            }
        });
    }

    @Override
    public void showData(Object object) {
        dismissLoading();
        //返回 布尔值表示已经异常
        if (object instanceof Boolean) {
            enddate.setText("加载失败");
            copyright.setText("请检查网络是否正常");
            showPicInfo();

        } else if (object instanceof List) {
//            LogUtil.d(((List<PicTab>) object).size());
            adapter.refresh((List<PicTab>) object);

        } else {

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    //某些原因加载失败
                    LogUtil.e("图片加载失败：" + exception);
                    enddate.setText("加载失败");
                    copyright.setText("请检查下网络是否正常");
                }
            });
            //重置mainPic的宽度-动态设置控件宽高
//            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mainPic.getLayoutParams();
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            mainPic.setLayoutParams(params);

            Picasso pic = builder.build();
            pic.load("file://" + object.toString()).error(error).into(mainPic);
            enddate.setText(Local.info.enddate);
            copyright.setText(Local.info.copyright);
            showPicInfo();
        }
    }


    @Override
    public void init_Data() {
        init_log();
        init_anim();
        presenter.initMainPic();
        init_recycler();



    }

    private void init_recycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new SpacesItemDecorationVertical(20));
        adapter= new TabAdapter(this);
        recycler.setAdapter(adapter);
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

    @Override
    public void showLoading() {
        super.showLoading();
//        mainPic.setImageResource(R.drawable.loading_);

    }
}
