package com.yu.hu.traveling.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yu.hu.library.util.DateUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.entity.user.TravelingUser;
import com.yu.hu.traveling.entity.user.User;
import com.yu.hu.traveling.entity.comment.Comment;
import com.yu.hu.traveling.entity.comment.Reply;
import com.yu.hu.traveling.holder.CommentViewHolder;
import com.yu.hu.traveling.util.GlideUtil;
import com.yu.hu.traveling.util.SpannableStringUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.adapter
 * 文件名：ReplyDetailAdapter
 * 创建者：HY
 * 创建时间：2019/7/3 14:59
 * 描述：  TODO
 */
public class ReplyDetailAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    /**
     * 头部元评论
     */
    private static final int COMMENT_HEAD = 1;

    private Context mContext;

    private Comment mComment;

    public ReplyDetailAdapter(Context context, Comment comment) {
        mContext = context;
        this.mComment = comment;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return COMMENT_HEAD;
        }
        return 0;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.replyBox.setVisibility(View.GONE);
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        //头部
        if (position == 0) {
            initHeadHolder(holder);
            return;
        }
        //其余部分
        initBodyHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mComment.getReplies().size() + 1;
    }

    /**
     * 初始化头部的评论
     */
    private void initHeadHolder(@NonNull CommentViewHolder holder) {
        GlideUtil.loadWithoutAnimate(mContext, mComment.getUserImg())
                .into(holder.userImg);

        holder.username.setText(mComment.getNickName());
        holder.content.setText(mComment.getContent());
        holder.commentTime.setText(DateUtil.fromNow(mComment.getCommentTime()));
        holder.replyBtn.setVisibility(View.GONE);
        holder.deleteBtn.setVisibility(View.GONE);
        holder.replyNum.setVisibility(View.VISIBLE);
        holder.replyNum.setText(mContext.getString(R.string.comments, mComment.getReplies().size()));
    }

    /**
     * 初始化中间部分的回复
     */
    private void initBodyHolder(@NonNull CommentViewHolder holder, int position) {
        Reply reply = mComment.getReplies().get(position - 1);
        GlideUtil.loadWithoutAnimate(mContext, reply.getUserImg())
                .into(holder.userImg);

        holder.username.setText(reply.getNickName());
        holder.content.setText(disPoseContent(reply));
        holder.replyNum.setVisibility(View.GONE);
        holder.commentTime.setText(DateUtil.fromNow(reply.getCommentTime()));
        holder.replyBtn.setOnClickListener(v -> {
            //todo
        });

        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null && currentUser.getId().equals(Long.valueOf(reply.getUserId()))) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setOnClickListener(v -> {
                //todo
            });
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }
    }

    //处理回复内容
    private SpannableString disPoseContent(Reply reply) {
        if (reply.getFlag() == Reply.FLAG_COMMENT) {
            return SpannableString.valueOf(reply.getContent());
        }
        SpannableString spannableString = new SpannableString(
                mContext.getResources().getString(R.string.str_reply_to2, reply.getToName(), reply.getContent())
        );
        int start = 3;
        int end = start + reply.getToName().length();
        ClickableSpan clickableReplySpan = SpannableStringUtil.getClickableReplySpan(mContext, widget -> {
            //todo
            //            Toast.makeText(mContext, "你点击了" + reply.getToId(), Toast.LENGTH_SHORT).show();
            //            Intent intent = new Intent(context, PersonalActivity.class);
            //            intent.putExtra(PersonalActivity.USER_ID, reply.getToId());
            //            context.startActivity(intent);
        });
        spannableString.setSpan(clickableReplySpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
