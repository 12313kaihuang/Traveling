package com.yu.hu.traveling.ui.detail;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.yu.hu.common.paging.AbsPagedListAdapter;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.ninegridlayout.OnGridItemClickListener;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentNoteDetailBinding;
import com.yu.hu.traveling.databinding.LayoutNoteDetailHeaderMultiBinding;
import com.yu.hu.traveling.databinding.LayoutNoteDetailHeaderSingleImgBinding;
import com.yu.hu.traveling.db.repository.NoteRepository;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.ui.preview.MediaPreviewFragment;
import com.yu.hu.traveling.ui.preview.PreviewViewModel;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/18 17:46
 **/
@FragmentDestination(pageUrl = "note/detail")
public class NoteDetailFragment extends AbsNoteDetailFragment<FragmentNoteDetailBinding> implements OnGridItemClickListener {

    public static final String PAGE_URL = "note/detail";
    private PreviewViewModel previewViewModel;

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        previewViewModel = new ViewModelProvider(mActivity).get(PreviewViewModel.class);
        //添加HeaderView
        int headerType = note.getDetailShowType();
        switch (headerType) {
            case SHOW_TYPE_MULTI:
                addMultiHeader(note);
                BarUtils.addMarginTopEqualStatusBarHeight(mDataBinding.getRoot());
                break;
            case SHOW_TYPE_SINGLE_IMG:
                addSingleImgHeader(note);
                BarUtils.addMarginTopEqualStatusBarHeight(mDataBinding.getRoot());
                break;
        }

        mDataBinding.setNote(note);
        finishRefresh(false);
        mEmptyView.setProgressBarVisibility(true);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mDataBinding.actionClose.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());
        mDataBinding.interactionLayout.getRoot().setOnClickListener(v -> onCommentItemClicked(null));
        mDataBinding.interactionLayout.deleteLayout.setOnClickListener(v -> mViewModel.deleteNote(note.noteId, (MainActivity) mActivity));
        mDataBinding.authorInfoLayout.getRoot().setOnClickListener(v -> mViewModel.toChatPage((MainActivity) mActivity, note.author));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_note_detail;
    }

    //添加多图HeaderView
    private void addMultiHeader(@NonNull Note note) {
        mDataBinding.title.setVisibility(View.GONE);
        mDataBinding.authorInfoLayout.authorInfo.setVisibility(View.VISIBLE);
        LayoutNoteDetailHeaderMultiBinding headerBinding = LayoutNoteDetailHeaderMultiBinding.inflate(mLayoutInflater, mRecyclerView, false);
        headerBinding.setNote(note);
        headerBinding.horizonImgView.setImgItems(note.getImgItems());
        headerBinding.horizonImgView.setOnItemClickListener(this);

        //添加HeaderView
        ((AbsPagedListAdapter) mAdapter).addHeaderView(headerBinding.getRoot());

        NoteRepository.getInstance().getNoteLiveData(note.noteId)
                .observe(this, newNote -> {
                    if (newNote == null) {
                        LogUtil.d(" addMultiHeader note == null ");
                    }
                    if (newNote != null) {
                        LogUtil.d("focus - " + newNote.author.hasFollow);
                    }
                    mDataBinding.authorInfoLayout.setNote(newNote);
                    mDataBinding.interactionLayout.setNote(newNote);
                });
    }

    //单张图片Header
    private void addSingleImgHeader(@NonNull Note note) {
        mDataBinding.title.setVisibility(View.VISIBLE);
        mDataBinding.authorInfoLayout.authorInfo.setVisibility(View.GONE);
        LayoutNoteDetailHeaderSingleImgBinding headerBinding =
                LayoutNoteDetailHeaderSingleImgBinding.inflate(mLayoutInflater, mRecyclerView, false);
        headerBinding.setNote(note);
        headerBinding.authorInfoLayout.getRoot().setOnClickListener(v ->
                mViewModel.toChatPage((MainActivity) mActivity, note.author));

        if (note.getImgItems().size() != 0) {
            GridItem item = note.getImgItems().get(0);
            headerBinding.imageView.bindData(item.width, item.height, 8, item.cover);
        }

        //添加HeaderView
        ((AbsPagedListAdapter) mAdapter).addHeaderView(headerBinding.getRoot());

        //滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                boolean visible = headerBinding.getRoot().getTop() <= -mDataBinding.titleLayout.getMeasuredHeight();
                mDataBinding.authorInfoLayout.getRoot().setVisibility(visible ? View.VISIBLE : View.GONE);
                mDataBinding.title.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });

        NoteRepository.getInstance().getNoteLiveData(note.noteId)
                .observe(this, newNote -> {
                    if (newNote == null) {
                        LogUtil.d(" addMultiHeader note == null ");
                    }
                    if (newNote != null) {
                        LogUtil.d("focus - " + newNote.author.hasFollow);
                    }
                    headerBinding.authorInfoLayout.setNote(newNote);
                    mDataBinding.authorInfoLayout.setNote(newNote);
                    mDataBinding.interactionLayout.setNote(newNote);
                });
    }

    @Override
    public void onItemClicked(List<GridItem> items, int position) {
        previewViewModel.setItems(items);
        ((MainActivity) mActivity).navigate(MediaPreviewFragment.PAGE_URL, MediaPreviewFragment.createArgs(position));
    }
}
