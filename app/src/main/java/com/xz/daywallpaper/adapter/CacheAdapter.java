package com.xz.daywallpaper.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xz.daywallpaper.PicDetailActivity;
import com.xz.daywallpaper.PicViewActivity;
import com.xz.daywallpaper.R;
import com.xz.daywallpaper.entity.PIc;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static android.view.ViewGroup.getChildMeasureSpec;

public class CacheAdapter extends RecyclerView.Adapter<CacheAdapter.ViewHolder> {
    private Context context;
    private List<PIc> list;

    public CacheAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void refresh(List<PIc> path) {
        list.addAll(path);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cache, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.enddateText.setText(list.get(i).getEnddate());
//        Picasso.get().load("file://"+list.get(i).getUrl()).error(R.drawable.error).into(viewHolder.picView);
        //Glide是实现平滑的图片列表滚动效果（滚动流畅）
        Glide.with(context)
                .load("file://" + list.get(i).getUrl())
                .error(R.drawable.error_2)
                .into(viewHolder.picView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView enddateText;
        private ImageView picView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            enddateText = itemView.findViewById(R.id.enddate_text);
            picView = itemView.findViewById(R.id.pic_view);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
//            context.startActivity(new Intent(context, PicViewActivity.class).putExtra("pic_uri",list.get(getLayoutPosition()).getUrl()));
            context.startActivity(new Intent(context, PicDetailActivity.class).putExtra("pic_uri", list.get(getLayoutPosition()).getUrl()));
        }
    }
}
