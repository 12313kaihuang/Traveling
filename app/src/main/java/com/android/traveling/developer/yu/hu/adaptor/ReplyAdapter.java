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
import android.widget.TextView;
import android.widget.Toast;

import com.android.traveling.R;
import com.android.traveling.developer.zhiming.li.ui.PersonalActivity;
import com.android.traveling.entity.comment.BaseComment;
import com.android.traveling.entity.comment.Reply;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.SpannableStringUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.DeleteCommentDialog;
import com.android.traveling.widget.dialog.ReplyDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.adaptor
 * 文件名：ReplyAdapter
 * 创建者：HY
 * 创建时间：2019/2/28 14:58
 * 描述：  回复 适配器
 */

@SuppressWarnings("unused")
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private Context context;
    private int noteId;  //文章id
    private int commentId; //所属评论的评论id
    private DataChangeListener dataChangeListener;
    private List<Reply> replyList;

    public ReplyAdapter(Context context, int noteId, int commentId, List<Reply> replyList) {
        this.context = context;
        this.noteId = noteId;
        this.commentId = commentId;
        this.dataChangeListener = (DataChangeListener) context;
        this.replyList = replyList;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment2, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Reply reply = replyList.get(position);
        Picasso.get().load(reply.getUserImg()).into(holder.user_img);
        holder.user_name.setText(reply.getNickName());
        holder.tv_comment_time.setText(DateUtil.fromNow(reply.getCommentTime()));
        holder.content.setText(disPoseContent(reply));
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        if (TravelingUser.isCurrentUser(reply.getUserId())) {
            holder.tv_delete.setVisibility(View.VISIBLE);
        } else {
            holder.tv_delete.setVisibility(View.INVISIBLE);
        }
        addEvents(holder, reply, position);
    }

    /**
     * 添加点击事件
     *
     * @param holder holder
     * @param reply  reply
     */
    private void addEvents(@NonNull ReplyViewHolder holder, Reply reply, int position) {
        //头像/用户名点击
        holder.user_img.setOnClickListener(v -> {
            UtilTools.toast(context, "点击了" + holder.user_name.getText() + "的信息");
            Intent intent = new Intent(context, PersonalActivity.class);
            intent.putExtra(PersonalActivity.USER_ID, reply.getUserId());
            context.startActivity(intent);
        });
        holder.user_name.setOnClickListener(v -> holder.user_img.callOnClick());

        //删除
        if (holder.tv_delete.getVisibility() == View.VISIBLE) {
            holder.tv_delete.setOnClickListener(v -> new DeleteCommentDialog(context, v12 -> {
                if (reply.getId() == null) {
                    UtilTools.toast(context, "reply.getId() == null");
                    return;
                }
                LogUtil.d("reply.getId()=" + reply.getId());
                Reply.deleteReply(reply.getId(), new Reply.DeleteCommentListener() {
                    @Override
                    public void onSuccess() {
                        replyList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, replyList.size() - position);
                        dataChangeListener.onDataChanged(replyList);
                        UtilTools.toast(context, "删除成功");
                    }

                    @Override
                    public void onFailure(String reason) {
                        UtilTools.toast(context, "删除失败" + reason);
                    }
                });
            }).show());
        }

        //点击回复
        holder.tv_reply.setOnClickListener(v -> {
            User currentUser = TravelingUser.checkLogin(context);
            if (currentUser == null) {
                return;
            }
            String hint = context.getResources().getString(R.string.str_reply_to, reply.getNickName());
            new ReplyDialog(context, hint, (v1, content) -> {
                //回复
                BaseComment baseComment = new BaseComment(Reply.FLAG_REPLY, noteId, commentId,
                        reply, currentUser.getUserId(), content
                );
                BaseComment.addComment(context, baseComment, new BaseComment.AddCommentListener() {
                    @Override
                    public void onSuccess(BaseComment baseComment1) {
                        LogUtil.d("position= " + position);
                        replyList.add(new Reply(currentUser, reply.getNickName(), baseComment1));
                        notifyItemInserted(replyList.size() - 1);
                        notifyItemChanged(replyList.size() - 1);
                        dataChangeListener.onDataChanged(replyList);
                    }

                    @Override
                    public void onFailure(String reason) {
                        UtilTools.toast(context, "发表失败：" + reason);
                    }
                });
            }).show();
        });
    }


    //处理回复内容
    private SpannableString disPoseContent(Reply reply) {
        if (reply.getFlag() == Reply.FLAG_COMMENT) {
            return SpannableString.valueOf(reply.getContent());
        }
        SpannableString spannableString = new SpannableString(
                context.getResources().getString(R.string.str_reply_to2, reply.getToName(), reply.getContent())
        );
        int start = 3;
        int end = start + reply.getToName().length();
        LogUtil.d("ReplyAdapter", "disPoseContent");
        ClickableSpan clickableReplySpan = SpannableStringUtil.getClickableReplySpan(context, widget -> {
            Toast.makeText(context, "你点击了" + reply.getToId(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, PersonalActivity.class);
            intent.putExtra(PersonalActivity.USER_ID, reply.getToId());
            context.startActivity(intent);
        });
        spannableString.setSpan(clickableReplySpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }


    static class ReplyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView user_img;
        TextView user_name;
        TextView content;
        TextView tv_comment_time;
        TextView tv_reply;
        TextView tv_delete;

        ReplyViewHolder(View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.tv_content);
            this.user_name = itemView.findViewById(R.id.tv_user_name);
            this.user_img = itemView.findViewById(R.id.user_img);
            this.tv_comment_time = itemView.findViewById(R.id.tv_comment_time);
            this.tv_reply = itemView.findViewById(R.id.tv_reply);
            this.tv_delete = itemView.findViewById(R.id.tv_delete);
        }
    }

    //数据发生变化时的回调接口
    public interface DataChangeListener {
        void onDataChanged(List<Reply> replies);
    }
}
