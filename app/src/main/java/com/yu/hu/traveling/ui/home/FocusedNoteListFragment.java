package com.yu.hu.traveling.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yu.hu.common.fragment.BaseListFragment;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.ninegridlayout.OnGridItemClickListener;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentSimpleListBinding;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.ui.detail.AbsNoteDetailFragment;
import com.yu.hu.traveling.ui.detail.NoteDetailFragment;
import com.yu.hu.traveling.ui.detail.SingleMediaNoteDetailFragment;
import com.yu.hu.traveling.ui.preview.MediaPreviewFragment;
import com.yu.hu.traveling.ui.preview.PreviewViewModel;
import com.yu.hu.traveling.utils.UserManager;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/30 16:48
 * <p>
 * 关注页面
 **/
public class FocusedNoteListFragment extends BaseListFragment<Note, FocusedNoteViewModel, FragmentSimpleListBinding> implements OnNoteItemClickListener, OnGridItemClickListener, OnRefreshListener, OnLoadMoreListener {

    private PreviewViewModel previewViewModel;

    private static final String KEY_TAG = "key_note_list_tag";

    private String tag = NoteViewModel.TAG_FOCUS;

    public static FocusedNoteListFragment newInstance() {
        return newInstance(NoteViewModel.TAG_FOCUS);
    }

    public static FocusedNoteListFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString(KEY_TAG, tag);
        FocusedNoteListFragment fragment = new FocusedNoteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void beforeInitView() {
        if (getArguments() != null) {
            tag = getArguments().getString(KEY_TAG, NoteViewModel.TAG_FOCUS);
        }
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        LogUtil.d("FocusedNoteListFragment onInitView");
        mDataBinding.refreshHeader.setColorSchemeResources(R.color.color_theme);
        //禁用动画 解决item刷新时图片闪烁问题
        mRecyclerView.setItemAnimator(null);
        previewViewModel = new ViewModelProvider(mActivity).get(PreviewViewModel.class);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        ((NoteAdapter) mAdapter).setGridItemClickListener(this);
        mDataBinding.refreshLayout.setDisableContentWhenRefresh(true);
        //是否启用列表惯性滑动到底部时自动加载更多
        mDataBinding.refreshLayout.setEnableAutoLoadMore(false);
        //上拉加载 下拉刷新监听
        mDataBinding.refreshLayout.setOnRefreshListener(this);
        mDataBinding.refreshLayout.setOnLoadMoreListener(this);
        mDataBinding.refreshLayout.autoRefreshAnimationOnly();

        //如果一开始已登录，这时退出登录需要加一个状态监听
        UserManager.get().addObserver(this, user -> checkLoginState());
        if (!UserManager.get().isLogin()) {
            checkLoginState();
        }
    }

    @Override
    protected FocusedNoteViewModel genericViewModel() {
        return new ViewModelProvider(this, new NoteViewModelFactory(tag)).get(FocusedNoteViewModel.class);
    }

    //根据是否登录来切换相应的模式
    private void checkLoginState() {
        LogUtil.d("FocusedNoteListFragment - checkLoginState");
        if (UserManager.get().isLogin()) {
            mDataBinding.refreshLayout.setEnableRefresh(true);
            mDataBinding.refreshLayout.setEnableLoadMore(true);
            mDataBinding.emptyView.setVisibility(View.GONE);
            mDataBinding.refreshLayout.autoRefresh();
            mDataBinding.emptyView.setTitle(R.string.try_to_refresh);
            mDataBinding.emptyView.setButton(getString(R.string.reload), v -> mDataBinding.refreshLayout.autoRefresh());
        } else {
            mDataBinding.refreshLayout.setEnableRefresh(false);
            mDataBinding.refreshLayout.setEnableLoadMore(false);
            mDataBinding.emptyView.setVisibility(View.VISIBLE);
            mDataBinding.emptyView.setEmptyViewVisibility(true);
            mDataBinding.emptyView.setTitle(R.string.focus_note_hint_with_noLogin);
            mDataBinding.emptyView.setButton(getString(R.string.to_login), v -> UserManager.get().login(mActivity).observe(this, user -> checkLoginState()));
        }
    }

    @Override
    public PagedListAdapter<Note, ? extends RecyclerView.ViewHolder> getAdapter() {
        return new NoteAdapter(this) {
            @Override
            public void onCurrentListChanged(@Nullable PagedList<Note> previousList, @Nullable PagedList<Note> currentList) {
                //这个方法是在我们每提交一次 pagelist对象到adapter 就会触发一次
                //每调用一次 adpater.submitlist
                if (previousList != null && currentList != null
                        && previewViewModel.isNeedScrollToTop() && isVisible()
                        && currentList.size() > previousList.size() && currentList.containsAll(previousList)) {
                    mRecyclerView.scrollToPosition(0);
                }
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_list;
    }

    @Override
    public void onItemClicked(Note note) {
        if (note.getDetailShowType() == AbsNoteDetailFragment.SHOW_TYPE_SINGLE_VIDEO) {
            ((MainActivity) mActivity).navigate(SingleMediaNoteDetailFragment.PAGE_URL, SingleMediaNoteDetailFragment.createArgs(note));
        } else {
            ((MainActivity) mActivity).navigate(NoteDetailFragment.PAGE_URL, NoteDetailFragment.createArgs(note));
        }
    }

    @Override
    public void submitList(PagedList<Note> result) {
        LogUtil.d("search submitList size = " + result.size() + ", " + GsonUtils.toJson(result));
        previewViewModel.setNeedScrollToTop(true);
        finishRefresh(result.size() > 0);
        mAdapter.submitList(result);
    }

    @Override
    public void onItemClicked(List<GridItem> items, int position) {
        previewViewModel.setItems(items);
        ((MainActivity) mActivity).navigate(MediaPreviewFragment.PAGE_URL, MediaPreviewFragment.createArgs(position));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.invalidateData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mDataBinding.refreshLayout.finishLoadMore(500);
    }

    @Override
    public void finishRefresh(boolean hasData) {
        PagedList<Note> currentList = mAdapter.getCurrentList();
        hasData = hasData || (currentList != null && currentList.size() > 0);
        //结束刷新
        RefreshState state = mDataBinding.refreshLayout.getState();
        if (state.isHeader) {
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
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        //noinspection ConstantConditions
        itemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.list_divider));
        return itemDecoration;
    }
}
