package com.yu.hu.traveling.ui.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.common.fragment.BaseListFragment;
import com.yu.hu.common.paging.AbsPagedListAdapter;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.view.EmptyView;
import com.yu.hu.emoji.EmojiCommentDialog;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentCommentDetailBinding;
import com.yu.hu.traveling.databinding.LayoutCommentDetailHeaderBinding;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.utils.InteractionPresenter;

/**
 * @author Hy
 * created on 2020/05/08 14:44
 * <p>
 * 评论详情
 **/
@FragmentDestination(pageUrl = "comment/detail")
public class CommentDetailFragment extends BaseListFragment<Comment, CommentViewModel, FragmentCommentDetailBinding> implements CommentAdapter.OnItemClickListener {

    public static final String PAGE_URL = "comment/detail";

    private static final String KEY_COMMENT = "key_comment";
    private static final String KEY_COMMENT_TYPE = "key_comment_type";

    private EmptyView mEmptyView;
    private int type;
    private Comment comment;

    public static Bundle createArgs(Comment comment, int type) {
        Bundle args = new Bundle();
        args.putInt(KEY_COMMENT_TYPE, type);
        args.putParcelable(KEY_COMMENT, comment);
        return args;
    }

    @Override
    protected void beforeInitView() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new RuntimeException("arguments == null");
        }
        type = arguments.getInt(KEY_COMMENT_TYPE, Comment.TYPE_NOTE_COMMENT);
        comment = arguments.getParcelable(KEY_COMMENT);
        if (comment == null) {
            throw new RuntimeException("comment == null");
        }
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mDataBinding.setComment(comment);
        addHeaderView(comment);
        finishRefresh(false);
        mEmptyView.setProgressBarVisibility(true);
    }

    private void addHeaderView(Comment comment) {
        LayoutCommentDetailHeaderBinding headerBinding = LayoutCommentDetailHeaderBinding.inflate(mLayoutInflater, mRecyclerView, false);
        headerBinding.setComment(comment);
        ((AbsPagedListAdapter) mAdapter).addHeaderView(headerBinding.getRoot());
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        ((CommentAdapter) mAdapter).setOnItemClickListener(this);
        mDataBinding.actionClose.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());
        mDataBinding.interactionLayout.getRoot().setOnClickListener(v -> publishReply(comment, false));
        mDataBinding.interactionLayout.deleteLayout.setOnClickListener(v -> onDeleteIconClicked(comment));
    }

    @Override
    public void finishRefresh(boolean hasData) {
        super.finishRefresh(hasData);
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
    public void onCommentItemClicked(Comment comment) {
        publishReply(comment, true);
    }

    @Override
    protected CommentViewModel genericViewModel() {
        return new ViewModelProvider(this, new DetailViewModelFactory(comment.id, type)).get(CommentViewModel.class);
    }

    @Override
    public CommentAdapter getAdapter() {
        return new CommentAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment_detail;
    }

    @Override
    public void onDeleteIconClicked(Comment comment) {
        mViewModel.deleteComment(mActivity, (CommentAdapter) mAdapter, comment);
    }

    @Override
    public void onReplyLayoutClicked(Comment comment) {

    }

    /**
     * @param isChildReply 用于判断是回复的本条评论{@code false}，还是回复的某条子评论{@code true}
     */
    private void publishReply(@NonNull Comment comment, boolean isChildReply) {
        if (!InteractionPresenter.checkLogin()) {
            return;
        }
        new EmojiCommentDialog.Builder()
                .hint(mViewModel.buildDialogHint(comment.author))
                .setPublishBtnColorResource(-1, Color.parseColor("#ff678f"))
                .setPublishBtnClickListener((dialog, content) -> {
                    if (TextUtils.isEmpty(content)) {
                        ToastUtils.showShort("评论不能为空");
                        return false;
                    }
                    if (isChildReply) {
                        LogUtil.d("testsss replyId = " + comment.author.id);
                        mViewModel.publishReply(this, mAdapter.getCurrentList(), type, content, comment.author.id);
                    } else {
                        mViewModel.publishReply(this, mAdapter.getCurrentList(), type, content, 0);
                    }
                    return true;
                })
                .show(getChildFragmentManager());
    }
}
