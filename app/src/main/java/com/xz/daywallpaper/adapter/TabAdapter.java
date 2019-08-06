package com.xz.daywallpaper.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xz.daywallpaper.R;
import com.xz.daywallpaper.entity.PicTab;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {
    private Context context;
    private List<PicTab> list;

    public TabAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void refresh(List<PicTab> path) {
        list.addAll(path);
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_progressbar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.progressBar.setProgress(list.get(i).getTag_confidence());
        viewHolder.tabName.setText(list.get(i).getTag_name());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private TextView tabName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tabName = itemView.findViewById(R.id.tab_name);

        }
    }
}
