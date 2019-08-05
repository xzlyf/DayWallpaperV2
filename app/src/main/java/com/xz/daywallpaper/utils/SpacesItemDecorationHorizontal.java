package com.xz.daywallpaper.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 修饰RecyclerView的Item的间距
 * 左右间距
 */
public class SpacesItemDecorationHorizontal extends RecyclerView.ItemDecoration {
    int space;
    public SpacesItemDecorationHorizontal(int space){
        this.space = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;

        if (parent.getChildPosition(view)==0){
            outRect.left = space;
        }
    }
}
