package com.yu.hu.traveling.ui.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedListAdapter;
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
import com.yu.hu.traveling.databinding.FragmentCompanionDetailBinding;
import com.yu.hu.traveling.databinding.LayoutCompanionDetailHeaderBinding;
import com.yu.hu.traveling.db.repository.CompanionRepository;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.model.Companion;
import com.yu.hu.traveling.utils.InteractionPresenter;

/**
 * @author Hy
 * created on 2020/04/24 14:34
 **/
@FragmentDestination(pageUrl = "companion/detail")
public class CompanionDetailFragment extends BaseListFragment<Comment, CommentViewModel, FragmentCompanionDetailBinding> implements CommentAdapter.OnItemClickListener {

    public static final String PAGE_URL = "companion/detail";

    private static final String KEY_COMPANION = "key_companion";
    private LayoutCompanionDetailHeaderBinding headerBinding;

    public static Bundle createArgs(Companion companion) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_COMPANION, companion);
        return args;
    }

    private EmptyView mEmptyView;
    private Companion companion;

    @Override
    protected void beforeInitView() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new RuntimeException("arguments == null");
        }
        companion = arguments.getParcelable(KEY_COMPANION);
        if (companion == null) {
            throw new RuntimeException("companion == null");
        }
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mDataBinding.setCompanion(companion);
        addHeaderView(companion);
        finishRefresh(false);
        mEmptyView.setProgressBarVisibility(true);
    }

    private void addHeaderView(Companion companion) {
        headerBinding = LayoutCompanionDetailHeaderBinding.inflate(mLayoutInflater, mRecyclerView, false);
        headerBinding.setCompanion(companion);
        headerBinding.authorInfoLayout.getRoot().setOnClickListener(v -> mViewModel.toChatPage((MainActivity) mActivity, companion.author));
        ((AbsPagedListAdapter) mAdapter).addHeaderView(headerBinding.getRoot());
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
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);

        ((CommentAdapter) mAdapter).setOnItemClickListener(this);
        mDataBinding.actionClose.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());
        mDataBinding.interactionLayout.getRoot().setOnClickListener(v -> onCommentItemClicked(null));
        mDataBinding.interactionLayout.deleteLayout.setOnClickListener(v -> mViewModel.deleteCompanion(companion.companionId, (MainActivity) mActivity));
        mDataBinding.authorInfoLayout.getRoot().setOnClickListener(v -> mViewModel.toChatPage((MainActivity) mActivity, companion.author));

        //滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                boolean visible = headerBinding.getRoot().getTop() <= -mDataBinding.titleLayout.getMeasuredHeight();
                mDataBinding.authorInfoLayout.getRoot().setVisibility(visible ? View.VISIBLE : View.GONE);
                mDataBinding.title.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });

        CompanionRepository.getInstance().getLiveCompanion(companion.companionId)
                .observe(this, newCompanion -> {
                    if (newCompanion == null) return;
                    mDataBinding.setCompanion(newCompanion);
                    headerBinding.setCompanion(newCompanion);
                });
    }

    @Override
    protected CommentViewModel genericViewModel() {
        return new ViewModelProvider(this, new DetailViewModelFactory(companion.companionId, Comment.TYPE_COMPANION)).get(CommentViewModel.class);
    }

    @Override
    public PagedListAdapter<Comment, CommentAdapter.ViewHolder> getAdapter() {
        return new CommentAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_companion_detail;
    }

    private boolean publishComment(String content, Comment comment) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("评论不能为空");
            return false;
        }
        if (comment != null) {
            LogUtil.d("发布回复");
            mViewModel.publishReply(Comment.TYPE_COMPANION_COMMENT, content, comment);

        } else {
            LogUtil.d("发布评论");
            mViewModel.publishComment(this, mAdapter.getCurrentList(), Comment.TYPE_COMPANION, content);
        }
        return true;
    }

    @Override
    public void onDeleteIconClicked(Comment comment) {
        mViewModel.deleteComment(mActivity, (CommentAdapter) mAdapter, comment);
    }

    @Override
    public void onReplyLayoutClicked(Comment comment) {
        ((MainActivity) mActivity).navigate(CommentDetailFragment.PAGE_URL, CommentDetailFragment.createArgs(comment, Comment.TYPE_COMPANION_COMMENT));
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
}
