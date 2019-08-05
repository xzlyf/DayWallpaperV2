package com.xz.daywallpaper.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 修饰RecyclerView的Item的间距
 * 垂直间距
 */
public class SpacesItemDecorationVertical extends RecyclerView.ItemDecoration {
    int space;
    public SpacesItemDecorationVertical(int space){
        this.space = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = space;
        outRect.bottom = space;

        if (parent.getChildPosition(view)==0){
            outRect.top = space;
        }
    }
}
