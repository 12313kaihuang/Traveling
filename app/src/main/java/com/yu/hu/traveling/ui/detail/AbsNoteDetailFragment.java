package com.yu.hu.traveling.ui.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.common.fragment.BaseListFragment;
import com.yu.hu.common.paging.AbsPagedListAdapter;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.view.EmptyView;
import com.yu.hu.emoji.EmojiCommentDialog;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.utils.InteractionPresenter;


/**
 * @author Hy
 * created on 2020/04/27 17:01
 * <p>
 * 封装抽象类
 **/
@SuppressWarnings("WeakerAccess")
public abstract class AbsNoteDetailFragment<D extends ViewDataBinding> extends BaseListFragment<Comment, CommentViewModel, D>
        implements CommentAdapter.OnItemClickListener {

    public static final int SHOW_TYPE_MULTI = 0;  //多图/视频
    public static final int SHOW_TYPE_SINGLE_IMG = 1; //单张图片
    public static final int SHOW_TYPE_SINGLE_VIDEO = 2; //单个视频

    protected static final String KEY_NOTE = "key_note";

    protected Note note;
    protected EmptyView mEmptyView;

    public static Bundle createArgs(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_NOTE, note);
        return args;
    }

    @Override
    protected void beforeInitView() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new RuntimeException("arguments == null");
        }
        note = arguments.getParcelable(KEY_NOTE);
        if (note == null) {
            throw new RuntimeException("note == null");
        }
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        ((CommentAdapter) mAdapter).setOnItemClickListener(this);
    }

    @Override
    public void finishRefresh(boolean hasData) {
        if (hasData) {
            if (mEmptyView != null) {
                mEmptyView.setEmptyViewVisibility(false);
                ((AbsPagedListAdapter) mAdapter).removeHeaderView(mEmptyView);
            }
        } else {
            if (mEmptyView == null) {
                mEmptyView = new EmptyView(mActivity);
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //layoutParams.topMargin = PixUtils.dp2px(40);
                mEmptyView.setLayoutParams(layoutParams);
                mEmptyView.setTitle(mActivity.getString(R.string.feed_comment_empty));
            }
            mEmptyView.setEmptyViewVisibility(true);
            ((AbsPagedListAdapter) mAdapter).addHeaderView(mEmptyView);
        }
    }

    @Override
    protected CommentViewModel genericViewModel() {
        return new ViewModelProvider(this, new DetailViewModelFactory(note.noteId)).get(CommentViewModel.class);
    }

    @Override
    public PagedListAdapter<Comment, CommentAdapter.ViewHolder> getAdapter() {
        return new CommentAdapter();
    }

    @Override
    public void onDeleteIconClicked(Comment comment) {
        mViewModel.deleteComment(mActivity,(CommentAdapter) mAdapter, comment);
    }

    @Override
    public void onCommentItemClicked(Comment comment) {
        if (!InteractionPresenter.checkLogin()) {
            return;
        }
        new EmojiCommentDialog.Builder()
                .hint(mViewModel.buildDialogHint(comment == null ? null : comment.author))
                .setPublishBtnColorResource(-1, Color.parseColor("#ff678f"))
                .setPublishBtnClickListener((dialog, content) -> publishComment(content, comment))
                .show(getChildFragmentManager());
    }

    @Override
    public void onReplyLayoutClicked(Comment comment) {
        ((MainActivity) mActivity).navigate(CommentDetailFragment.PAGE_URL, CommentDetailFragment.createArgs(comment, Comment.TYPE_NOTE_COMMENT));
    }

    protected boolean publishComment(String content, Comment comment) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("评论不能为空");
            return false;
        }
        if (comment != null) {
            LogUtil.d("发布回复");
            mViewModel.publishReply(Comment.TYPE_NOTE_COMMENT, content, comment);

        } else {
            LogUtil.d("发布评论");
            mViewModel.publishComment(this, mAdapter.getCurrentList(), Comment.TYPE_NOTE, content);
        }
        return true;
    }
}
