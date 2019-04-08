package com.android.traveling.developer.yu.hu.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.note.Note;
import com.android.traveling.util.UtilTools;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.adaptor
 * 文件名：SearchResultAdapter
 * 创建者：HY
 * 创建时间：2019/3/3 22:21
 * 描述：  用搜索文章结果中的recycleView
 */

public class
SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private String searchContent;
    private Context context;
    private List<Note> newsList;
    private OnItemClickListener onItemClickListener = null;


    public SearchResultAdapter(Context context, String searchContent, List<Note> newsList) {
        this.context = context;
        this.searchContent = searchContent;
        this.newsList = newsList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = newsList.get(position);
        holder.title.setText(disposeTitle(note.getTitle()));
        holder.content.setText(note.getContent());
        holder.likeAndCommentNum.setText(context.getResources().getString(
                R.string.like_and_comment_num, note.getLikeNum(), note.getCommentNum()
        ));
        if (note.getImgList().size() == 0) {
            holder.img.setVisibility(View.GONE);
        } else {
            holder.img.setVisibility(View.VISIBLE);
            Picasso.get().load(note.getImgList().get(0))
                    .placeholder(R.drawable.err_img_bg)
                    .error(R.drawable.err_img_bg)
                    .into(holder.img);
        }
        //实现点击效果
        holder.itemView.setOnClickListener(v -> {
            UtilTools.toast(v.getContext(), "点击了" + position);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, note, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    /**
     * 高亮关键字
     *
     * @param title 文章标题
     * @return SpannableString
     */
    private SpannableString disposeTitle(String title) {
        int start = title.indexOf(searchContent);
        SpannableString spannableString = new SpannableString(title);
        if (start < 0) {
            return spannableString;
        }
        int end = start + searchContent.length();
        spannableString.setSpan(new ForegroundColorSpan(0xFFFFBE00), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    public interface OnItemClickListener {
        void onItemClick(View v, Note note, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content;
        ImageView img;
        TextView likeAndCommentNum;

        ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.tv_note_title);
            this.content = itemView.findViewById(R.id.tv_note_content);
            this.img = itemView.findViewById(R.id.img_note);
            this.likeAndCommentNum = itemView.findViewById(R.id.tv_like_comment_num);
        }
    }
}
