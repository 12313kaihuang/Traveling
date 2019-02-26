package com.android.traveling.developer.yu.hu.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.ui.ReplyDetailActivity;
import com.android.traveling.developer.zhiming.li.ui.PersonalActivity;
import com.android.traveling.entity.note.Comment;
import com.android.traveling.entity.note.Reply;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.UtilTools;
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

    private Context context;
    private List<Comment> commentList = new ArrayList<>();  //数据集合
    //    private static final int HEADER_TYPE=0;  //头

    public CommentAdaptor(Context context, List<Comment> commentList) {
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
        showReplies(holder, comment.getReplies());
        addEvents(holder, comment);
    }

    /**
     * 绑定事件
     *
     * @param holder  holder
     * @param comment comment
     */
    private void addEvents(CommentViewHolder holder, Comment comment) {

        holder.user_img.setOnClickListener(v -> {
            UtilTools.toast(context, "点击了" + holder.user_name.getText() + "的信息");
            Intent intent = new Intent(context, PersonalActivity.class);
            intent.putExtra(PersonalActivity.USER_ID, comment.getUserId());
            context.startActivity(intent);
        });
        holder.user_name.setOnClickListener(v -> holder.user_img.callOnClick());
        holder.tv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilTools.toast(context, "点击了回复");
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /**
     * 显示回复
     *
     * @param holder  holder
     * @param replies replies
     */
    private void showReplies(CommentViewHolder holder, List<Reply> replies) {
        switch (replies.size()) {
            case 3:
                holder.tv_comment_3.setText(
                        context.getResources().getString(R.string.all_comment, replies.size()));
                holder.tv_comment_3.setOnClickListener(v -> {
                    UtilTools.toast(context, "显示所有评论");
                    Intent intent = new Intent(context, ReplyDetailActivity.class);
                    intent.putExtra(ReplyDetailActivity.USER_ID,"回复详情");
                    context.startActivity(intent);
                });
            case 2:
                holder.tv_comment_2.setText(getSpannableString(replies.get(1)));
                //设置高亮背景颜色为透明色
                holder.tv_comment_2.setHighlightColor(context.getResources().getColor(android.R.color.transparent));   //设置高亮背景颜色为透明色
                //要加上这句点击事件才会触发
                holder.tv_comment_2.setMovementMethod(LinkMovementMethod.getInstance());
            case 1:
                holder.tv_comment_1.setText(getSpannableString(replies.get(0)));
                //设置高亮背景颜色为透明色
                holder.tv_comment_1.setHighlightColor(context.getResources().getColor(android.R.color.transparent));   //设置高亮背景颜色为透明色
                //要加上这句点击事件才会触发
                holder.tv_comment_1.setMovementMethod(LinkMovementMethod.getInstance());
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
            ClickableSpan to = getClickableSpan(widget -> {
                Toast.makeText(context, "你点击了" + reply.getToId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PersonalActivity.class);
                intent.putExtra(PersonalActivity.USER_ID, reply.getToId());
                context.startActivity(intent);
            });
            int start = reply.getNickName().length() + 4;
            spannableString.setSpan(to, start, start + reply.getToName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        ClickableSpan clickableSpan = getClickableSpan(widget -> {
            Toast.makeText(context, "你点击了" + reply.getUserId(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, PersonalActivity.class);
            intent.putExtra(PersonalActivity.USER_ID, reply.getUserId());
            context.startActivity(intent);
        });
        spannableString.setSpan(clickableSpan, 0, reply.getNickName().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * @param mClickableSpan 自定义接口
     * @return ClickableSpan
     */
    private ClickableSpan getClickableSpan(mClickableSpan mClickableSpan) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                mClickableSpan.onClick(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.blue_comment));   //设置字体颜色
                ds.setUnderlineText(false);    //设置是否显示下划线
                ds.clearShadowLayer();   //阴影
            }
        };
    }

    //自定义接口
    private interface mClickableSpan {
        void onClick(View widget);
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
