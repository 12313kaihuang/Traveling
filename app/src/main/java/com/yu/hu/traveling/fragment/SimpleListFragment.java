package com.yu.hu.traveling.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yu.hu.common.fragment.BaseListFragment;
import com.yu.hu.common.fragment.RoomListFragment;
import com.yu.hu.common.paging.RoomViewModel;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentSimpleListBinding;

/**
 * @param <T>  list bean
 * @param <VM> ViewModel类型
 * @author Hy
 * created on 2020/04/18 21:55
 * <p>
 * 实现了一个简单的展示列表数据并可刷新的listFragment
 * 有一个简单的布局 {@code R.layout.fragment_simple_list}，
 * 如果需要布局比较复杂需要自定义还是集成{@link BaseListFragment}
 **/
public abstract class SimpleListFragment<T, VM extends RoomViewModel<?, T>> extends RoomListFragment<T, VM, FragmentSimpleListBinding> implements OnRefreshListener, OnLoadMoreListener, RoomViewModel.OnNetDataLoadListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_list;
    }

    @CallSuper
    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        //设置header颜色
        //mDataBinding.refreshHeader.setColorSchemeResources(R.color.color_theme, R.color.lightYellow);
        mDataBinding.refreshHeader.setColorSchemeResources(R.color.color_theme);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);

        mDataBinding.refreshLayout.setDisableContentWhenRefresh(true);
        //是否启用列表惯性滑动到底部时自动加载更多
        mDataBinding.refreshLayout.setEnableAutoLoadMore(false);
        //上拉加载 下拉刷新监听
        mDataBinding.refreshLayout.setOnRefreshListener(this);
        mDataBinding.refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.refresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mDataBinding.refreshLayout.finishLoadMore(500);
    }

    @Override
    public void finishRefresh(boolean hasData) {
        PagedList<T> currentList = mAdapter.getCurrentList();
        hasData = hasData || (currentList != null && currentList.size() > 0);
        //结束刷新
        LogUtil.d(getClass().getSimpleName() + "AAS  finishRefresh hasData - " + hasData);
        RefreshState state = mDataBinding.refreshLayout.getState();
        if (state.isHeader) {
            LogUtil.d("AAS  finishRefresh isHeader");
            mDataBinding.refreshLayout.finishRefresh();
        } else if (state.isFooter) {
            mDataBinding.refreshLayout.finishLoadMore();
        }

        mDataBinding.emptyView.setVisibility(hasData ? View.GONE : View.VISIBLE);
        mDataBinding.emptyView.setEmptyViewVisibility(!hasData);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mDataBinding.refreshLayout.finishRefresh();
            mDataBinding.refreshLayout.finishLoadMore();
        }
    }

    @Override
    protected RecyclerView.ItemDecoration createItemDecoration() {
        //默认给列表中的Item 一个 10dp的ItemDecoration
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        //noinspection ConstantConditions
        itemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.list_divider));
        return itemDecoration;
    }
}
