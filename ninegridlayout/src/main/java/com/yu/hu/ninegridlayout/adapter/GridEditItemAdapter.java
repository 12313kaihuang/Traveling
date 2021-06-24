package com.yu.hu.ninegridlayout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yu.hu.ninegridlayout.NineGridView;
import com.yu.hu.ninegridlayout.OnGridItemClickListener;
import com.yu.hu.ninegridlayout.databinding.ItemEditGridBinding;
import com.yu.hu.ninegridlayout.entity.GridItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/25 19:14
 **/
public class GridEditItemAdapter extends ListAdapter<GridItem, GridEditItemAdapter.ViewHolder> {

    private NineGridView.Config viewConfig;

    private OnGridItemClickListener itemClickListener;

    public void setItemClickListener(OnGridItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setConfig(NineGridView.Config config) {
        this.viewConfig = config;
    }

    public GridEditItemAdapter() {
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEditGridBinding binding = ItemEditGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GridItem item = getItem(position);
        if (position == getItemCount() - 1) {
            holder.bindAddIcon(viewConfig.itemSize, viewConfig.drawableResource);
        } else {
            holder.bind(item, viewConfig.itemSize);
        }

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
            if (item == currentList.get(i) || item.equals(currentList.get(i))) {
                return i;
            }
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemEditGridBinding mDataBinding;
        private Context context;

        ViewHolder(ItemEditGridBinding binding) {
            super(binding.getRoot());
            this.context = binding.getRoot().getContext();
            this.mDataBinding = binding;
        }

        void bindAddIcon(int size, @DrawableRes int drawable) {
            setItemSize(size);
            mDataBinding.deleteIcon.setVisibility(View.GONE);
            mDataBinding.img.setImageResource(drawable);
        }

        public void bind(final GridItem gridItem, int size) {
            setItemSize(size);

            mDataBinding.deleteIcon.setVisibility(View.VISIBLE);
            mDataBinding.setItem(gridItem);
            //通过glide获取图片宽高
            Glide.with(mDataBinding.img).load(gridItem.coverPath)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            gridItem.width = resource.getIntrinsicWidth();
                            gridItem.height = resource.getIntrinsicHeight();
                            mDataBinding.img.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            if (placeholder != null) {
                                gridItem.width = placeholder.getIntrinsicWidth();
                                gridItem.height = placeholder.getIntrinsicHeight();
                                mDataBinding.img.setImageDrawable(placeholder);
                            }
                        }
                    });

            mDataBinding.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<GridItem> currentList = new ArrayList<>(getCurrentList());
                    Iterator<GridItem> iterator = currentList.iterator();
                    while (iterator.hasNext()) {
                        GridItem next = iterator.next();
                        if (next.equals(gridItem)) {
                            iterator.remove();
                            break;
                        }
                    }
                    submitList(currentList);
                }
            });
        }

        void setItemSize(int size) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
            itemView.setLayoutParams(layoutParams);
        }
    }
}
