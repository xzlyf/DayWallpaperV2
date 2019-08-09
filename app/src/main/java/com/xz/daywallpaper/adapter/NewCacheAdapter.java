package com.xz.daywallpaper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xz.daywallpaper.R;
import com.xz.daywallpaper.constant.Local;
import com.xz.daywallpaper.entity.PIc;
import com.xz.daywallpaper.utils.FileUtil;
import com.xz.daywallpaper.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljm
 */
public class NewCacheAdapter extends RecyclerView.Adapter<NewCacheAdapter.SlideViewHolder> {

    private Context mContext;
    private List<PIc> mList;


    public NewCacheAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();

    }

    public void refresh(List<PIc> path) {
        mList.addAll(path);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewCacheAdapter.SlideViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_cache_new, viewGroup, false);
        return new SlideViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final NewCacheAdapter.SlideViewHolder holder, int i) {
        if (!holder.mDeleteTv.hasOnClickListeners()) {
            holder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除图片文件
                    FileUtil.deleteFile(mList.get(holder.getAdapterPosition()).getUrl());//执行删除
                    //删除数据文件
                    SharedPreferencesUtil.deleteDate(mContext,mList.get(holder.getAdapterPosition()).getEnddate());

                    mList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
        }
        holder.enddateText.setText(mList.get(i).getEnddate());
        //Glide是实现平滑的图片列表滚动效果（滚动流畅）
        Glide.with(mContext)
                .load("file://" + mList.get(i).getUrl())
                .error(R.drawable.error_2)
                .into(holder.picView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SlideViewHolder extends RecyclerView.ViewHolder {
        private TextView mDeleteTv;
        private TextView enddateText;
        private ImageView picView;

        private SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            mDeleteTv = itemView.findViewById(R.id.tv_delete);
            enddateText = itemView.findViewById(R.id.enddate_text);
            picView = itemView.findViewById(R.id.pic_view);
        }
    }
}