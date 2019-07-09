package com.yu.hu.traveling.holder;

import android.view.View;
import android.widget.TextView;

import com.yu.hu.traveling.R;
import com.yu.hu.traveling.widget.ReplyBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.holder
 * 文件名：CommentViewHolder
 * 创建者：HY
 * 创建时间：2019/7/3 15:20
 * 描述：  一个评论item对应的一个ViewHolder
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView userImg; //用户头像
    public TextView username; //用户名

    public TextView content; //评论内容
    public TextView commentTime; //评论时间
    public TextView replyBtn; //回复
    public TextView deleteBtn; //删除

    public ReplyBox replyBox; //回复

    /**
     * 评论数  用于回复详情的head部分{@link com.yu.hu.traveling.adapter.ReplyDetailAdapter}
     */
    public TextView replyNum;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        replyBox = itemView.findViewById(R.id.reply_box);
        userImg = itemView.findViewById(R.id.user_img);
        username = itemView.findViewById(R.id.tv_user_name);
        content = itemView.findViewById(R.id.tv_content);
        commentTime = itemView.findViewById(R.id.tv_comment_time);
        replyBtn = itemView.findViewById(R.id.tv_reply);
        deleteBtn = itemView.findViewById(R.id.tv_delete);
        replyNum = itemView.findViewById(R.id.reply_num);
    }
}