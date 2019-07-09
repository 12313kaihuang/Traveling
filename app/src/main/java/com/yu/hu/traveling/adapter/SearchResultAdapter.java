package com.yu.hu.traveling.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.entity.note.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.adaptor
 * 文件名：SearchResultAdapter
 * 创建者：HY
 * 创建时间：2019/3/3 22:21
 * 描述：  用搜索文章结果中的recycleView
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private String mSearchContent;
    private Context mContext;
    private List<Note> mNewsList;
    private OnItemClickListener mItemClickListener = null;


    public SearchResultAdapter(Context context, String searchContent, List<Note> newsList) {
        this.mContext = context;
        this.mSearchContent = searchContent;
        this.mNewsList = newsList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = mNewsList.get(position);
        holder.title.setText(disposeTitle(note.getTitle()));
        holder.content.setText(note.getContent());
        holder.likeAndCommentNum.setText(mContext.getResources().getString(
                R.string.like_and_comment_num, note.getLikeNum(), note.getCommentNum()
        ));
        if (note.getImgList().size() == 0) {
            holder.img.setVisibility(View.GONE);
        } else {
            holder.img.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(note.getImgList().get(0))
                    .placeholder(R.drawable.img_bg_place_holder)
                    .error(R.drawable.ic_error_img)
                    .into(holder.img);
        }
        //实现点击效果
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, note, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    /**
     * 高亮关键字
     *
     * @param title 文章标题
     * @return SpannableString
     */
    private SpannableString disposeTitle(String title) {
        int start = title.indexOf(mSearchContent);
        SpannableString spannableString = new SpannableString(title);
        if (start < 0) {
            return spannableString;
        }
        int end = start + mSearchContent.length();
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
