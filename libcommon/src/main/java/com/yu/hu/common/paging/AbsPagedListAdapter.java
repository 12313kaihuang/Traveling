package com.yu.hu.common.paging;


import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.hu.common.binding.BindingViewHolder;

/**
 * @param <T>  Java Bean
 * @param <VH> ViewHolder type
 * @author Hy
 * created on 2020/04/17 16:55
 * <p>
 * 一个能够添加HeaderView,FooterView的PagedListAdapter。
 * 解决了添加HeaderView和FooterView时 RecyclerView定位不准确的问题 见{@link AdapterDataObserverProxy}
 * <p>
 * ViewHolder需要继承{@link BindingViewHolder}
 **/
@SuppressWarnings("unused")
public abstract class AbsPagedListAdapter<T, VH extends RecyclerView.ViewHolder> extends PagedListAdapter<T, VH> {

    //此处相比于HashMap性能更高效
    private SparseArray<View> mHeaders = new SparseArray<>();
    private SparseArray<View> mFooters = new SparseArray<>();

    //headView footerView的 viewType范围  避免与普通的item相冲突
    private int BASE_ITEM_TYPE_HEADER = 100_000;
    private int BASE_ITEM_TYPE_FOOTER = 200_000;

    protected AbsPagedListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    /**
     * 子类重写{@link #onCreateViewHolder2(ViewGroup, int)}
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaders.indexOfKey(viewType) >= 0) {
            View view = mHeaders.get(viewType);
            return (VH) new RecyclerView.ViewHolder(view) {
            };
        }

        if (mFooters.indexOfKey(viewType) >= 0) {
            View view = mFooters.get(viewType);
            return (VH) new RecyclerView.ViewHolder(view) {
            };
        }
        return onCreateViewHolder2(parent, viewType);
    }

    public abstract VH onCreateViewHolder2(ViewGroup parent, int viewType);

    /**
     * 子类重写{@link #onBindViewHolder2(RecyclerView.ViewHolder, int)}
     */
    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        //列表中正常类型的itemView的 position 咱们需要减去添加headerView的个数
        position = position - mHeaders.size();
        onBindViewHolder2(holder, position);
    }

    public abstract void onBindViewHolder2(VH holder, int position);

    @Override
    public final int getItemCount() {
        return super.getItemCount() + mHeaders.size() + mFooters.size();
    }

    /**
     * 子类重写{@link #getItemViewType2(int)}
     */
    @Override
    public final int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            //返回该position对应的headerview的  viewType
            return mHeaders.keyAt(position);
        }

        if (isFooterPosition(position)) {
            //footer类型的，需要计算一下它的position实际大小
            position = position - getOriginalItemCount() - mHeaders.size();
            return mFooters.keyAt(position);
        }
        //重新计算子类中的position
        position = position - mHeaders.size();
        return getItemViewType2(position);
    }

    @SuppressWarnings("WeakerAccess")
    public int getItemViewType2(int position) {
        return 0;
    }

    /**
     * 获取原始数据item个数  即submitList进来的集合数据个数
     */
    @SuppressWarnings("WeakerAccess")
    public int getOriginalItemCount() {
        return super.getItemCount();
    }

    /**
     * 子类重写{@link #onViewAttachedToWindow2(RecyclerView.ViewHolder)}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (!isHeaderPosition(holder.getAdapterPosition()) && !isFooterPosition(holder.getAdapterPosition())) {
            this.onViewAttachedToWindow2((VH) holder);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void onViewAttachedToWindow2(VH holder) {

    }

    /**
     * 子类重写{@link #onViewDetachedFromWindow2(RecyclerView.ViewHolder)}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (!isHeaderPosition(holder.getAdapterPosition()) && !isFooterPosition(holder.getAdapterPosition())) {
            this.onViewDetachedFromWindow2((VH) holder);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void onViewDetachedFromWindow2(VH holder) {

    }

    /**
     * 设置一个observer代理，
     * 这样在更新数据时才不会发生position错乱的问题
     * 详见{@link AdapterDataObserverProxy}
     */
    @CallSuper
    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(new AdapterDataObserverProxy(observer));
    }

    /**
     * 添加HeaderView
     */
    public void addHeaderView(View view) {
        //判断给View对象是否还没有处在mHeaders数组里面
        if (mHeaders.indexOfValue(view) < 0) {
            mHeaders.put(BASE_ITEM_TYPE_HEADER++, view);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加FooterView
     */
    public void addFooterView(View view) {
        //判断给View对象是否还没有处在mFooters数组里面
        if (mFooters.indexOfValue(view) < 0) {
            mFooters.put(BASE_ITEM_TYPE_FOOTER++, view);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除HeaderView
     */
    public void removeHeaderView(View view) {
        int index = mHeaders.indexOfValue(view);
        if (index < 0) return;
        mHeaders.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 移除FooterView
     */
    public void removeFooterView(View view) {
        int index = mFooters.indexOfValue(view);
        if (index < 0) return;
        mFooters.removeAt(index);
        notifyDataSetChanged();
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

    public int getFooterCount() {
        return mFooters.size();
    }

    /**
     * 判断这个位置的item是否是HeaderView
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaders.size();
    }

    /**
     * 判断这个位置的item是否是FooterView
     */
    private boolean isFooterPosition(int position) {
        return position >= getOriginalItemCount() + mHeaders.size();
    }

    //如果我们先添加了headerView,而后网络数据回来了再更新到列表上
    //由于Paging在计算列表上item的位置时 并不会顾及我们有没有添加headerView，就会出现列表定位的问题
    //实际上 RecyclerView#setAdapter方法，它会给Adapter注册了一个AdapterDataObserver
    //咱么可以代理registerAdapterDataObserver()传递进来的observer。在各个方法的实现中，把headerView的个数算上，再中转出去即可
    private class AdapterDataObserverProxy extends RecyclerView.AdapterDataObserver {
        private RecyclerView.AdapterDataObserver mObserver;

        AdapterDataObserverProxy(RecyclerView.AdapterDataObserver observer) {
            mObserver = observer;
        }

        @Override
        public void onChanged() {
            mObserver.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mObserver.onItemRangeChanged(positionStart + mHeaders.size(), itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            mObserver.onItemRangeChanged(positionStart + mHeaders.size(), itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mObserver.onItemRangeInserted(positionStart + mHeaders.size(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mObserver.onItemRangeRemoved(positionStart + mHeaders.size(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mObserver.onItemRangeMoved(fromPosition + mHeaders.size(), toPosition + mHeaders.size(), itemCount);
        }
    }
}
