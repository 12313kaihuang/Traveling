package com.android.traveling.developer.yu.hu.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.developer.yu.hu.ui.ReplyDetailActivity;
import com.android.traveling.developer.zhiming.li.ui.PersonalActivity;
import com.android.traveling.entity.note.BaseComment;
import com.android.traveling.entity.note.Comment;
import com.android.traveling.entity.note.Reply;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.SpannableStringUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.ReplyDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.adaptor
 * 文件名：CommentAdaptor
 * 创建者：HY
 * 创建时间：2019/2/25 9:22
 * 描述：  评论 适配器
 */

public class CommentAdaptor extends RecyclerView.Adapter<CommentAdaptor.CommentViewHolder> {

    private int noteId;
    private Context context;
    private ReplyDialog replyDialog;
    private List<Comment> commentList = new ArrayList<>();  //数据集合
    //    private static final int HEADER_TYPE=0;  //头

    public CommentAdaptor(Context context, int noteId, List<Comment> commentList) {
        this.noteId = noteId;
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createCommentViewHolder(parent);
    }


    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        Picasso.get().load(comment.getUserImg()).into(holder.user_img);
        holder.user_name.setText(comment.getNickName());
        holder.content.setText(comment.getContent());
        holder.tv_comment_time.setText(DateUtil.fromNow(comment.getCommentTime()));
        showReplies(holder, comment);
        addEvents(holder, comment, position);
    }

    /**
     * 绑定事件
     *
     * @param holder  holder
     * @param comment comment
     */
    private void addEvents(CommentViewHolder holder, Comment comment, int position) {

        holder.user_img.setOnClickListener(v -> {
            UtilTools.toast(context, "点击了" + holder.user_name.getText() + "的信息");
            Intent intent = new Intent(context, PersonalActivity.class);
            intent.putExtra(PersonalActivity.USER_ID, comment.getUserId());
            context.startActivity(intent);
        });
        holder.user_name.setOnClickListener(v -> holder.user_img.callOnClick());

        holder.ll_c.setOnClickListener(v -> startToReplyDetailActivity(comment));

        //回复
        holder.tv_reply.setOnClickListener(v -> {
            User currentUser = TravelingUser.checkLogin(context);
            if (currentUser == null) {
                return;
            }
            if (replyDialog == null) {
                String hint = context.getResources().getString(R.string.str_reply_to, comment.getNickName());
                replyDialog = new ReplyDialog(context, hint, (v1, content) -> {

                    //回复
                    BaseComment baseComment = new BaseComment(Reply.FLAG_COMMENT, noteId,
                            comment.getId(), currentUser.getUserId(), content
                    );
                    BaseComment.addComment(context, baseComment, new BaseComment.AddCommentListener() {
                        @Override
                        public void onSuccess(BaseComment baseComment) {
                            LogUtil.d("adapter " + commentList.get(position).getReplies().size());
                            LogUtil.d("position= " + position);
                            comment.addReply(currentUser, baseComment);
                            commentList.set(position, comment);
//                            dataChangedListener.onDataChanged(commentList);
                            LogUtil.d("adapter " + commentList.get(position).getReplies().size());
//                            notifyItemRangeChanged(position,commentList.size());
                            replyDialog = null;
                        }

                        @Override
                        public void onFailure(String reason) {
                            UtilTools.toast(context, "发表失败：" + reason);
                            replyDialog = null;
                        }
                    });
                });
            }
            replyDialog.show();
        });
    }

    /**
     * 跳转到回复详情页面
     *
     * @param comment comment
     */
    private void startToReplyDetailActivity(Comment comment) {
        Intent intent = new Intent(context, ReplyDetailActivity.class);
        intent.putExtra(ReplyDetailActivity.COMMENT, comment);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /**
     * 显示回复
     *
     * @param holder  holder
     * @param comment comment
     */
    private void showReplies(CommentViewHolder holder, Comment comment) {
        List<Reply> replies = comment.getReplies();
        LogUtil.d("replies.size()="+replies.size());
        int tv_comment_num = replies.size() >= 3 ? 3 : replies.size();
        switch (tv_comment_num) {
            case 3:
                holder.tv_comment_3.setText(
                        context.getResources().getString(R.string.all_comment, replies.size()));
                holder.tv_comment_3.setOnClickListener(v -> startToReplyDetailActivity(comment));
            case 2:
                holder.tv_comment_2.setText(getSpannableString(replies.get(1)));
                //设置高亮背景颜色为透明色
                holder.tv_comment_2.setHighlightColor(context.getResources().getColor(android.R.color.transparent));   //设置高亮背景颜色为透明色
                //要加上这句点击事件才会触发
                holder.tv_comment_2.setMovementMethod(LinkMovementMethod.getInstance());
                holder.tv_comment_2.setOnClickListener(v -> startToReplyDetailActivity(comment));
            case 1:
                holder.tv_comment_1.setText(getSpannableString(replies.get(0)));
                //设置高亮背景颜色为透明色
                holder.tv_comment_1.setHighlightColor(context.getResources().getColor(android.R.color.transparent));   //设置高亮背景颜色为透明色
                //要加上这句点击事件才会触发
                holder.tv_comment_1.setMovementMethod(LinkMovementMethod.getInstance());
                holder.tv_comment_1.setOnClickListener(v -> startToReplyDetailActivity(comment));
        }
        if (replies.size() < 3) {
            holder.tv_comment_3.setVisibility(View.GONE);
        }
        if (replies.size() < 2) {
            holder.tv_comment_2.setVisibility(View.GONE);
        }
        if (replies.size() < 1) {
            holder.tv_comment_1.setVisibility(View.GONE);
        }
    }

    /**
     * 区别开每条评论的用户名和评论/回复内容
     *
     * @param reply reply
     * @return SpannableString
     */
    private SpannableString getSpannableString(Reply reply) {
        SpannableString spannableString;
        if (reply.getFlag() == Reply.FLAG_COMMENT) {
            spannableString = new SpannableString(
                    context.getResources().getString(R.string.comment, reply.getNickName(), reply.getContent()));
        } else {
            spannableString = new SpannableString(
                    context.getResources().getString(R.string.comment_reply, reply.getNickName(), reply.getToName(), reply.getContent()));
            ClickableSpan to = SpannableStringUtil.getClickableReplySpan(context, widget -> {
                Toast.makeText(context, "你点击了" + reply.getToId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PersonalActivity.class);
                intent.putExtra(PersonalActivity.USER_ID, reply.getToId());
                context.startActivity(intent);
            });
            int start = reply.getNickName().length() + 4;
            spannableString.setSpan(to, start, start + reply.getToName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        ClickableSpan clickableSpan = SpannableStringUtil.getClickableReplySpan(context, widget -> {
            Toast.makeText(context, "你点击了" + reply.getUserId(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, PersonalActivity.class);
            intent.putExtra(PersonalActivity.USER_ID, reply.getUserId());
            context.startActivity(intent);
        });
        spannableString.setSpan(clickableSpan, 0, reply.getNickName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private CommentViewHolder createCommentViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new CommentViewHolder(view);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        CircleImageView user_img;
        TextView user_name;
        TextView content;
        TextView tv_comment_1;
        TextView tv_comment_2;
        TextView tv_comment_3;
        TextView tv_comment_time;
        TextView tv_reply;
        LinearLayout ll_c;

        CommentViewHolder(View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.tv_content);
            this.user_name = itemView.findViewById(R.id.tv_user_name);
            this.user_img = itemView.findViewById(R.id.user_img);
            this.tv_comment_1 = itemView.findViewById(R.id.tv_comment_1);
            this.tv_comment_2 = itemView.findViewById(R.id.tv_comment_2);
            this.tv_comment_3 = itemView.findViewById(R.id.tv_comment_3);
            this.tv_comment_time = itemView.findViewById(R.id.tv_comment_time);
            this.tv_reply = itemView.findViewById(R.id.tv_reply);
            this.ll_c = itemView.findViewById(R.id.ll_c);
        }
    }
}
