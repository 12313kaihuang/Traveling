package com.yu.hu.ninegridlayout.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yu.hu.ninegridlayout.OnGridItemClickListener;
import com.yu.hu.ninegridlayout.databinding.ItemGridBinding;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.ninegridlayout.utils.PixUtils;
import com.yu.hu.ninegridlayout.NineGridView;

import java.util.List;


/**
 * @author Hy
 * created on 2020/04/17 21:24
 **/
public class GridItemAdapter extends ListAdapter<GridItem, GridItemAdapter.ViewHolder> {

    private NineGridView.Config viewConfig;

    private OnGridItemClickListener itemClickListener;

    public void setItemClickListener(OnGridItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public GridItemAdapter() {
        super(new DiffUtil.ItemCallback<GridItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull GridItem oldItem, @NonNull GridItem newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull GridItem oldItem, @NonNull GridItem newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    public void setConfig(NineGridView.Config config) {
        this.viewConfig = config;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGridBinding binding = ItemGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GridItem item = getItem(position);
        if (position == viewConfig.maxCount - 1 && super.getItemCount() > viewConfig.maxCount) {
            //当前是最后一个view
            item.setMoreCount(super.getItemCount() - getItemCount());
        }
        holder.bind(item, viewConfig.itemSize);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClicked(getCurrentList(), getItemPosition(item));
                }
            }
        });
    }

    private int getItemPosition(GridItem item) {
        List<GridItem> currentList = getCurrentList();
        for (int i = 0; i < currentList.size(); i++) {
            if (item.equals(currentList.get(i))) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return Math.min(super.getItemCount(), viewConfig.maxCount);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemGridBinding mDataBinding;

        ViewHolder(@NonNull ItemGridBinding binding) {
            super(binding.getRoot());
            mDataBinding = binding;
        }

        void bind(GridItem item, int size) {

            if (viewConfig.itemCount == 1) {

                if (viewConfig.width <= 0 || viewConfig.height <= 0) {
                    setSizeAutomatic(item);
                    //return;
                }

                //只有1个item 宽高自适应  最宽 最高均为屏幕宽度
                setSize(viewConfig.width, viewConfig.height, viewConfig.spanSpacing, viewConfig.availableSize, viewConfig.availableSize);
            } else {
                //否则等大小方形图片展示
                GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
                layoutParams.width = size;
                layoutParams.height = size;
                //解决复用只有item的ViewHolder时  第一张图片margin属性保留导致布局有误bug
                layoutParams.leftMargin = 0;
                layoutParams.topMargin = 0;
                layoutParams.bottomMargin = 0;
                itemView.setLayoutParams(layoutParams);
            }

            mDataBinding.setItem(item);
        }

        //自己获取图片大小并设置
        private void setSizeAutomatic(final GridItem item) {
            //noinspection deprecation
            Glide.with(itemView).load(item.cover).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width, height, viewConfig.spanSpacing, viewConfig.availableSize, viewConfig.availableSize);
                    //mDataBinding.img.setImageDrawable(resource);
                    //mDataBinding.videoIcon.setVisibility(item.showVideoIcon() ? View.VISIBLE : View.GONE);
                }
            });
        }

        /**
         * 根据图片/视频宽高自适应设置大小
         *
         * @param width     width
         * @param height    height
         * @param spacing   spacing item间距  dp
         * @param maxWidth  maxWidth
         * @param maxHeight maxHeight
         */
        private void setSize(int width, int height, int spacing, int maxWidth, int maxHeight) {
            int finalWidth, finalHeight;
            if (width > height) {
                //宽大于高 则宽为屏幕宽度  高等比缩放
                finalWidth = maxWidth;
                finalHeight = (int) (height / (width * 1.0f / finalWidth));
            } else {
                //高大于宽  高为屏幕宽度 宽等比缩放
                finalHeight = maxHeight;
                finalWidth = (int) (width / (height * 1.0f / finalHeight));
            }

            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            params.width = finalWidth;
            params.height = finalHeight;
            if (params instanceof GridLayoutManager.LayoutParams) {
                //height > width ? PixUtils.dp2px(spacing) : 0
                int spSpacing = PixUtils.dp2px(spacing);
                ((GridLayoutManager.LayoutParams) params).leftMargin = spSpacing;
                ((GridLayoutManager.LayoutParams) params).topMargin = spSpacing;
                ((GridLayoutManager.LayoutParams) params).bottomMargin = spSpacing;
            }
            itemView.setLayoutParams(params);
        }
    }
}
