package com.xz.daywallpaper.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        initView(rootView);
        initDate(mContext);
        return rootView;
    }


    /**
     * 初始化布局
     *
     * @return
     */
    protected abstract int getLayout();

    /**
     * 初始化控件
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     * 初始化数据-绑定数据等
     *
     * @param mContext
     */
    protected abstract void initDate(Context mContext);


}
