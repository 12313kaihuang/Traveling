package com.yu.hu.traveling.ui.detail;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.yu.hu.common.binding.BindingViewHolder;
import com.yu.hu.common.paging.AbsPagedListAdapter;
import com.yu.hu.ninegridlayout.utils.PixUtils;
import com.yu.hu.traveling.BR;
import com.yu.hu.traveling.databinding.ItemCommentBinding;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.utils.UserManager;

/**
 * @author Hy
 * created on 2020/04/18 21:05
 **/
public class CommentAdapter extends AbsPagedListAdapter<Comment, CommentAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;

    CommentAdapter() {
        super(new DiffUtil.ItemCallback<Comment>() {
            @Override
            public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        ItemCommentBinding binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder2(ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends BindingViewHolder<Comment, ItemCommentBinding> {

        ViewHolder(@NonNull ItemCommentBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void bind(Comment comment) {
            mDataBinding.setComment(comment);
            mDataBinding.setVariable(BR.currentUserId, UserManager.get().getUserId());
            if (!TextUtils.isEmpty(comment.imageUrl)) {
                mDataBinding.commentCover.bindData(comment.width, comment.height, 0, PixUtils.dp2px(200), PixUtils.dp2px(200), comment.imageUrl);
            }
            mDataBinding.deleteBtn.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onDeleteIconClicked(comment);
                }
            });
            mDataBinding.replyCount.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onReplyLayoutClicked(comment);
                }
            });
            itemView.setOnClickListener(v -> onItemClicked(comment));
        }

        private void onItemClicked(Comment comment) {
            if (onItemClickListener != null) {
                onItemClickListener.onCommentItemClicked(comment);
            }
        }
    }

    public interface OnItemClickListener {
        //删除按钮
        void onDeleteIconClicked(Comment comment);

        //回复个数
        void onReplyLayoutClicked(Comment comment);

        //整个item其他地方
        void onCommentItemClicked(Comment comment);
    }
}
