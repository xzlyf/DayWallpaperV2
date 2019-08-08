package com.xz.daywallpaper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xz.com.log.LogUtil;
import com.xz.daywallpaper.adapter.CacheAdapter;
import com.xz.daywallpaper.base.BaseActivity;
import com.xz.daywallpaper.constant.Local;
import com.xz.daywallpaper.entity.PIc;
import com.xz.daywallpaper.utils.SpacesItemDecorationVH;

import java.util.List;

public class CacheActivity extends BaseActivity {
    private RecyclerView recycler;
    private CacheAdapter adapter;
    private TextView total;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_cache;
    }

    @Override
    public void findID() {

        recycler = findViewById(R.id.recycler_pic);
        total = findViewById(R.id.total);
    }

    @Override
    public void init_Data() {
        setTitle("缓存");
        init_recycler();
        presenter.findLocalPic();
    }

    private void init_recycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new SpacesItemDecorationVH(20));
        adapter = new CacheAdapter(this);
        recycler.setAdapter(adapter);
    }

    @Override
    public void showData(Object object) {
        if (object instanceof Boolean){
            mToast("空空如也");
        }else {
            total.setText("已缓存了"+ Local.info.picTotal+"张图");
            adapter.refresh((List<PIc>) object);
        }
    }
}
