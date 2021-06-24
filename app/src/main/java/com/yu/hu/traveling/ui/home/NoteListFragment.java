package com.yu.hu.traveling.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.ninegridlayout.OnGridItemClickListener;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.fragment.SimpleListFragment;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.ui.detail.AbsNoteDetailFragment;
import com.yu.hu.traveling.ui.detail.NoteDetailFragment;
import com.yu.hu.traveling.ui.detail.SingleMediaNoteDetailFragment;
import com.yu.hu.traveling.ui.preview.MediaPreviewFragment;
import com.yu.hu.traveling.ui.preview.PreviewViewModel;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/17 16:54
 **/
public class NoteListFragment extends SimpleListFragment<Note, NoteViewModel> implements OnNoteItemClickListener, OnGridItemClickListener {

    private static final String KEY_TAG = "key_tag";

    private String tag;

    private PreviewViewModel previewViewModel;
    private int targetId;  //用于跳转他人的个人页面使用

    public static NoteListFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString(KEY_TAG, tag);
        NoteListFragment fragment = new NoteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NoteListFragment setTargetUserId(int targetId) {
        this.targetId = targetId;
        return this;
    }

    @Override
    protected String generateLogTag() {
        return super.generateLogTag() + String.format(" - tag = %s - ", tag);
    }

    @Override
    protected void beforeInitView() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            tag = arguments.getString(KEY_TAG);
        }
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        previewViewModel = new ViewModelProvider(mActivity).get(PreviewViewModel.class);
        ((NoteAdapter) mAdapter).setGridItemClickListener(this);
        LogUtil.d("tag = " + tag);
        if (NoteViewModel.TAG_SELF_NOTE.equals(tag) || NoteViewModel.TAG_SELF_STRATEGY.equals(tag)) {
            mDataBinding.refreshLayout.setEnableRefresh(false);
            mDataBinding.refreshLayout.setEnableLoadMore(false);
            mDataBinding.emptyView.setVisibility(View.VISIBLE);
            mDataBinding.emptyView.setBackgroundResource(R.color.white);
            mDataBinding.emptyView.setProgressBarVisibility(true);
            mViewModel.refresh();
        } else {
            mDataBinding.refreshLayout.autoRefresh();
        }

        //禁用动画 解决item刷新时图片闪烁问题
        mRecyclerView.setItemAnimator(null);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        if (NoteViewModel.TAG_SELF_NOTE.equals(tag) || NoteViewModel.TAG_SELF_STRATEGY.equals(tag)) {
            mDataBinding.emptyView.setButton(null, null);
            mDataBinding.emptyView.setTitle(R.string.none_publish_hint);
        } else {
            mDataBinding.emptyView.setTitle(R.string.try_to_refresh);
            mDataBinding.emptyView.setButton(getString(R.string.reload), v -> mDataBinding.refreshLayout.autoRefresh());
        }
    }

    @Override
    public void submitList(PagedList<Note> list) {
        previewViewModel.setNeedScrollToTop(true);
        super.submitList(list);
    }

    @Override
    public PagedListAdapter<Note, NoteAdapter.ViewHolder> getAdapter() {
        return new NoteAdapter(this) {
            @Override
            public void onCurrentListChanged(@Nullable PagedList<Note> previousList, @Nullable PagedList<Note> currentList) {
                //这个方法是在我们每提交一次 pagelist对象到adapter 就会触发一次
                //每调用一次 adpater.submitlist
                if (previousList != null && currentList != null
                        && previewViewModel.isNeedScrollToTop() && isVisible()
                        && currentList.size() > previousList.size() && currentList.containsAll(previousList)) {
                    LogUtil.d("onCurrentListChanged " + currentList.containsAll(previousList));
                    mRecyclerView.scrollToPosition(0);
                }
            }
        };
    }

    @Override
    protected NoteViewModel genericViewModel() {
        NoteViewModel noteViewModel = new ViewModelProvider(this, new NoteViewModelFactory(tag))
                .get(NoteViewModel.class);
        if (targetId != 0) {
            noteViewModel.setTargetId(targetId);
        }
        return noteViewModel;
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
    public void onItemClicked(List<GridItem> items, int position) {
        previewViewModel.setItems(items);
        ((MainActivity) mActivity).navigate(MediaPreviewFragment.PAGE_URL, MediaPreviewFragment.createArgs(position));
    }
}
