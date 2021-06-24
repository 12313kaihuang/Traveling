package com.yu.hu.traveling.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;

import com.yu.hu.common.binding.BindingViewHolder;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.ItemSearchHistoryBinding;
import com.yu.hu.traveling.databinding.LayoutSearchHistoryViewBinding;
import com.yu.hu.traveling.db.repository.SearchHistoryRepository;
import com.yu.hu.traveling.model.SearchHistory;

import java.util.Collections;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/20 9:53
 * <p>
 * 搜索历史展示列表
 * 可以通过{@link #submitList(List)}手动更新，也可通过
 **/
@SuppressWarnings("unused")
public class SearchHistoryView extends LinearLayout {

    private Context mContext;
    private HistoryAdapter mAdapter;

    private OnClearBtnClickListener onClearBtnClickListener;
    private OnItemClickListener onItemClickListener;

    public SearchHistoryView(Context context) {
        this(context, null);
    }

    public SearchHistoryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchHistoryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SearchHistoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;

        LogUtil.d("啥意思啊");
        setBackgroundResource(R.color.white);
        setOrientation(VERTICAL);
        init();
    }

    private void init() {

        mAdapter = new HistoryAdapter();
        LayoutSearchHistoryViewBinding mDataBinding = LayoutSearchHistoryViewBinding.inflate(LayoutInflater.from(mContext), this, true);

        mDataBinding.searchRecyclerView.setAdapter(mAdapter);
        mDataBinding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mDataBinding.clearBtn.setOnClickListener(this::handleClearBtnClicked);
    }

    /**
     * 添加列表自动刷新监听
     */
    public void setAutoRefreshObserver(LifecycleOwner owner) {
        SearchHistoryRepository.getRecentSearchHistories().observe(owner, this::submitList);
    }

    /**
     * 手动提交搜索历史
     */
    public void submitList(List<SearchHistory> histories) {
        if (histories == null || histories.size() == 0) {
            setVisibility(INVISIBLE);
            mAdapter.submitList(Collections.emptyList());
            return;
        }
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        LogUtil.d("size = " + histories.size());
        mAdapter.submitList(histories);
    }

    /**
     * 添加一条搜索记录
     */
    public void addHistory(@NonNull String searchContent) {
        SearchHistoryRepository.getInstance().insert(new SearchHistory(searchContent));
    }

    /**
     * 清空按钮点击事件监听
     *
     * @see #handleClearBtnClicked(View)  有默认的处理逻辑 ，也支持自定义
     */
    public void setOnClearBtnClickListener(OnClearBtnClickListener onClearBtnClickListener) {
        this.onClearBtnClickListener = onClearBtnClickListener;
    }

    /**
     * item点击事件监听
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 清空按钮事件监听
     */
    private void handleClearBtnClicked(View v) {
        if (onClearBtnClickListener != null) {
            onClearBtnClickListener.onClicked(v);
            return;
        }
        SearchHistoryRepository.getInstance().clearAll();
    }

    /**
     * HistoryAdapter
     */
    class HistoryAdapter extends ListAdapter<SearchHistory, ViewHolder> {

        HistoryAdapter() {
            super(new DiffUtil.ItemCallback<SearchHistory>() {
                @Override
                public boolean areItemsTheSame(@NonNull SearchHistory oldItem, @NonNull SearchHistory newItem) {
                    return oldItem.searchContent.equals(newItem.searchContent);
                }

                @Override
                public boolean areContentsTheSame(@NonNull SearchHistory oldItem, @NonNull SearchHistory newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return ViewHolder.create(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SearchHistory item = getItem(position);
            holder.bind(item);

            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener == null) {
                    return;
                }
                boolean update = onItemClickListener.onItemClicked(item.searchContent);
                if (update) {
                    SearchHistoryRepository.getInstance().update(item);
                }
            });
        }
    }

    /**
     * ViewHolder
     */
    static class ViewHolder extends BindingViewHolder<SearchHistory, ItemSearchHistoryBinding> {

        static ViewHolder create(@NonNull ViewGroup parent) {
            ItemSearchHistoryBinding binding = ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        ViewHolder(@NonNull ItemSearchHistoryBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void bind(SearchHistory data) {
            mDataBinding.setContent(data.searchContent);
            mDataBinding.clearBtn.setOnClickListener(v -> SearchHistoryRepository.getInstance().delete(data));
        }
    }

    /**
     * 清空按钮事件监听
     */
    public interface OnClearBtnClickListener {
        void onClicked(View v);
    }

    /**
     * Item点击事件监听
     */
    public interface OnItemClickListener {
        /**
         * @return 是否更新搜索记录 {@code true}  更新搜索记录，否则不更新
         */
        boolean onItemClicked(@NonNull String searchContent);

    }
}
