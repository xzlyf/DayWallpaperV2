package com.xz.daywallpaper;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xz.daywallpaper.adapter.TabAdapter;
import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.constant.Local;
import com.xz.daywallpaper.entity.PicTab;
import com.xz.daywallpaper.entity.Update;
import com.xz.daywallpaper.utils.PackageUtil;
import com.xz.daywallpaper.utils.SharedPreferencesUtil;
import com.xz.daywallpaper.utils.SpacesItemDecorationVertical;
import com.xz.daywallpaper.utils.ThreadUtil;
import com.xz.daywallpaper.widget.MenuDialog;
import com.xz.daywallpaper.widget.UpdateDialog;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView menu;
    private CardView infoView;
    private CardView headview;
    private TextView enddate;
    private TextView copyright;
    private ImageView mainPic;
    private RecyclerView recycler;
    private TabAdapter adapter;
    private boolean isClick = true;
    private NestedScrollView scrollView;
    private ImageView mLikeIt;
    private ImageView mHateIt;


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
        mLikeIt = findViewById(R.id.like_it);
        mHateIt = findViewById(R.id.hate_it);
        mHateIt.setOnClickListener(this);
        mLikeIt.setOnClickListener(this);


        //长按查看图片
        mainPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(MainActivity.this, PicViewActivity.class).putExtra("pic_uri", Local.picTDir));
                return true;
            }
        });


    }

    @Override
    public void showData(Object object) {
        dismissLoading();
        if (object instanceof Boolean) {
            //加载失败反馈
            enddate.setText("加载失败");
            copyright.setText("请检查网络是否正常");

        } else if (object instanceof List) {
            //标签列表
            adapter.refresh((List<PicTab>) object);

        } else if (object instanceof Bitmap) {
            //模糊图片反馈
            mainPic.setImageBitmap((Bitmap) object);
        } else if (object instanceof Update) {
            //升级反馈
            /**
             * 检查更新
             */
            UpdateDialog dialog = new UpdateDialog(MainActivity.this, R.style.update_dialog);
            dialog.create();
            dialog.setMsg(((Update) object).getMsg());
            dialog.setVersionName(((Update) object).getName());
            dialog.setDownloadLink(((Update) object).getLink());
            dialog.setLevel(((Update) object).getLevel());
            dialog.show();

        } else {
            enddate.setText(Local.info.enddate);
            copyright.setText(Local.info.copyright);

            update_state();//更新下状态
        }
    }


    @Override
    public void init_Data() {
        //初始化一些数据，这些都要放进initActivity中初始化
        Local.info.isHate = SharedPreferencesUtil.getState(this, "is_hate", false);
        Local.info.LocalVersionCode = PackageUtil.getVersionCode(this);
        //============================================
        showLoading();
        init_anim();
        presenter.initMainPic();
        init_recycler();
        init_update();
    }

    /**
     * 获取本地版本信息
     */

    /**
     * 检查更新
     */
    private void init_update() {

        ThreadUtil.runInThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);//3秒后执行检查更新操作
                presenter.checkUpdate();
            }
        });

    }


    private void update_state() {
        if (Local.info.isHate) {
            mHateIt.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            presenter.blurryPic(Local.picTDir);

        } else {
            mHateIt.setImageTintList(null);
            Glide.with(this).load(Local.picTDir).into(mainPic);

        }
    }

    private void init_recycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new SpacesItemDecorationVertical(20));
        adapter = new TabAdapter(this);
        recycler.setAdapter(adapter);
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
            case R.id.like_it:
                break;
            case R.id.hate_it:
                Local.info.isHate = !Local.info.isHate;
                SharedPreferencesUtil.saveState(this, "is_hate", Local.info.isHate);
                update_state();
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
        //改为使用属性动画
        infoView.animate().translationYBy(300f).setDuration(800);
        headview.animate().translationYBy(-300f).setDuration(800);
        //infoView.startAnimation(weiyi2_Res);
        //headview.startAnimation(weiyi_Res);
        isClick = true;
        mLikeIt.setEnabled(true);
        mHateIt.setEnabled(true);
    }

    private void hidePicInfo() {
        //改为使用属性动画
        infoView.animate().translationY(-300f).setDuration(800);
        headview.animate().translationY(300f).setDuration(800);
        //infoView.startAnimation(weiyi2);
        //headview.startAnimation(weiyi);
        mLikeIt.setEnabled(false);
        mHateIt.setEnabled(false);
        isClick = false;
    }

    /**
     * 补间动画
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
