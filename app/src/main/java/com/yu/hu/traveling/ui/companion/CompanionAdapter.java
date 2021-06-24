package com.yu.hu.traveling.ui.companion;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.yu.hu.common.binding.BindingViewHolder;
import com.yu.hu.common.paging.AbsPagedListAdapter;
import com.yu.hu.traveling.databinding.ItemCompanionBinding;
import com.yu.hu.traveling.model.Companion;

/**
 * @author Hy
 * created on 2020/04/24 12:08
 **/
public class CompanionAdapter extends AbsPagedListAdapter<Companion, CompanionAdapter.ViewHolder> {

    private OnCompanionItemClickListener mListener;

    @SuppressWarnings("WeakerAccess")
    protected CompanionAdapter(OnCompanionItemClickListener listener) {
        super(new DiffUtil.ItemCallback<Companion>() {
            @Override
            public boolean areItemsTheSame(@NonNull Companion oldItem, @NonNull Companion newItem) {
                return oldItem.companionId == newItem.companionId;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Companion oldItem, @NonNull Companion newItem) {
                return oldItem.equals(newItem);
            }
        });
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return ViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder2(ViewHolder holder, int position) {
        Companion item = getItem(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) mListener.onCompanionItemClicked(item);
        });
    }

    static class ViewHolder extends BindingViewHolder<Companion, ItemCompanionBinding> {

        static ViewHolder create(ViewGroup parent) {
            ItemCompanionBinding binding = ItemCompanionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        ViewHolder(@NonNull ItemCompanionBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void bind(Companion data) {
            mDataBinding.setCompanion(data);
        }
    }

    public interface OnCompanionItemClickListener {
        void onCompanionItemClicked(Companion companion);
    }
}
