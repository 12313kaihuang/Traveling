package com.yu.hu.ninegridlayout.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.hu.ninegridlayout.databinding.ItemGridBinding;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.ninegridlayout.utils.BindingAdapters;
import com.yu.hu.ninegridlayout.OnGridItemClickListener;
import com.yu.hu.ninegridlayout.utils.ViewUtils;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/19 0:13
 **/
public class ImgItemAdapter extends ListAdapter<GridItem, ImgItemAdapter.ViewHolder> {

    private OnGridItemClickListener itemClickListener;

    public void setItemClickListener(OnGridItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ImgItemAdapter() {
        super(new DiffUtil.ItemCallback<GridItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull GridItem oldItem, @NonNull GridItem newItem) {
                return TextUtils.equals(oldItem.cover, newItem.cover)
                        && TextUtils.equals(oldItem.url, newItem.url);
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
        ItemGridBinding binding = ItemGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GridItem item = getItem(position);
        holder.bind(item);
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemGridBinding mDataBinding;

        ViewHolder(@NonNull ItemGridBinding binding) {
            super(binding.getRoot());
            mDataBinding = binding;
        }

        void bind(GridItem item) {
            ViewUtils.setSize(mDataBinding.img, item);
            mDataBinding.setItem(item);
            mDataBinding.coverImg.setVisibility(View.VISIBLE);
            BindingAdapters.setBlurImgUrl(mDataBinding.coverImg, item.cover);
        }
    }
}
