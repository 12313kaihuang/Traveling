package com.android.traveling.developer.yu.hu.adaptor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.traveling.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.adaptor
 * 文件名：CommentAdaptor
 * 创建者：HY
 * 创建时间：2019/2/25 9:22
 * 描述：  评论 适配器
 */

public class CommentAdaptor extends RecyclerView.Adapter<CommentAdaptor.CommentViewHolder> {

    private List<String> commentList = new ArrayList<>();  //数据集合
    //    private static final int HEADER_TYPE=0;  //头

    public CommentAdaptor(List<String> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createCommentViewHolder(parent);
    }


    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.content.setText(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    private CommentViewHolder createCommentViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new CommentViewHolder(view);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView content;

        CommentViewHolder(View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.tv_content);
        }
    }
}
