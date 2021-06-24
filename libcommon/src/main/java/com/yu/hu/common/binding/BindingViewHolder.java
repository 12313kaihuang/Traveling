package com.yu.hu.common.binding;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Hy
 * created on 2020/04/17 17:08
 * <p>
 * 封装一个DataBinding类型的ViewHolder
 **/
@SuppressWarnings("unused")
public abstract class BindingViewHolder<T, D extends ViewDataBinding> extends RecyclerView.ViewHolder
        implements IBinding<T> {

    protected D mDataBinding;

    public BindingViewHolder(@NonNull D dataBinding) {
        super(dataBinding.getRoot());
        this.mDataBinding = dataBinding;
    }

    /**
     * 用于绑定数据
     */
    @Override
    public abstract void bind(T data);
}
