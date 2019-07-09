package com.yu.hu.traveling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yu.hu.library.util.DateUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.entity.note.Note;
import com.yu.hu.traveling.holder.LoadingHolder;
import com.yu.hu.traveling.util.GlideUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.adapter
 * 文件名：NotesAdapter
 * 创建者：HY
 * 创建时间：2019/7/8 18:23
 * 描述：  游记列表
 *
 * @see com.yu.hu.traveling.fragment.MyFragment
 */
public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 加载中
     */
    private static final int VIEW_TYPE_LOADING = 0;

    /**
     * 显示游记的标记位
     */
    private static final int VIEW_TYPE_NOTES = 1;

    /**
     * 没有发表文章
     */
    private static final int VIEW_TYPE_NO_DATA = 2;

    private Context mContext;

    private List<Note> mNoteList;

    public NotesAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mNoteList == null) {
            //加载中
            return VIEW_TYPE_LOADING;
        }

        if (mNoteList.size() == 0) {
            //没有发表过文章
            return VIEW_TYPE_NO_DATA;
        }
        //显示游记内容
        return VIEW_TYPE_NOTES;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_LOADING:
            case VIEW_TYPE_NO_DATA:
                view = layoutInflater.inflate(R.layout.item_loading, parent, false);
                return new LoadingHolder(view);
            default:
                view = layoutInflater.inflate(R.layout.item_note, parent, false);
                return new NoteViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoteViewHolder) {
            //加载游记
            NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
            initNoteViewHolder(noteViewHolder, mNoteList.get(position));
            return;
        }

        LoadingHolder loadingHolder = (LoadingHolder) holder;
        if (mNoteList != null && mNoteList.size() == 0) {
            //no data
            loadingHolder.progressBar.setVisibility(View.GONE);
            loadingHolder.hint.setText(mContext.getString(R.string.no_notes));
        } else {
            //loading
            loadingHolder.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mNoteList == null) {
            return 1;
        }
        return mNoteList.size();
    }

    public void setNoteList(List<Note> noteList) {
        this.mNoteList = noteList;
        notifyDataSetChanged();
    }

    /**
     * 初始化holder
     *
     * @param holder NoteViewHolder
     * @param note   游记对象
     */
    private void initNoteViewHolder(NoteViewHolder holder, Note note) {
        //用户头像
        GlideUtil.loadWithoutAnimate(mContext, note.getReleasePeople().getImgUrl())
                .into(holder.userImg);

        //游记图片
        if (note.getImgList().size() != 0) {
            holder.noteImg.setVisibility(View.VISIBLE);
            GlideUtil.load(mContext, note.getImgList().get(0))
                    .into(holder.noteImg);
        } else {
            holder.noteImg.setVisibility(View.GONE);
        }

        //用户名 游记内容等
        holder.username.setText(note.getReleasePeople().getNickName());
        holder.publishTime.setText(DateUtil.fromNow(note.getCreateTime()));
        holder.userLevel.setText(String.format(mContext.getString(R.string.level), "LV."
                , note.getReleasePeople().getLevel()));
        holder.noteContent.setText(note.getContent());

        setFlag(holder.noteFlag, note.getTag()); //标记

        //是否喜欢
        if (holder.isLiked) {
            holder.likeIcon.setImageResource(R.drawable.ic_like2);
        } else {
            holder.likeIcon.setImageResource(R.drawable.ic_like);
        }

        //喜欢评论数
        holder.likeNum.setText(String.valueOf(note.getLikeNum()));
        holder.commitNum.setText(String.valueOf(note.getCommentNum()));
    }

    //设置news类别
    private void setFlag(TextView noteFlag, int flag) {
        switch (flag) {
            case 1:
                noteFlag.setText(R.string.news_flag1);
                noteFlag.setTextColor(Const.NEWS_FLAG1_COLOR);
                noteFlag.setBackgroundResource(R.drawable.news_flag1_bg);
                break;
            case 2:
                noteFlag.setText(R.string.news_flag2);
                noteFlag.setTextColor(Const.NEWS_FLAG2_COLOR);
                noteFlag.setBackgroundResource(R.drawable.news_flag2_bg);
                break;
            case 3:
                noteFlag.setText(R.string.news_flag3);
                noteFlag.setTextColor(Const.NEWS_FLAG3_COLOR);
                noteFlag.setBackgroundResource(R.drawable.news_flag3_bg);
                break;
        }
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImg; //用户头像
        TextView username; //用户名
        TextView userLevel; //等级
        TextView publishTime; //发布时间

        TextView noteFlag; //游记标记 游记/攻略
        ImageView noteImg; //游记图片
        TextView noteContent; //游记内容

        ImageView likeIcon;  //喜欢图标
        TextView likeNum; //喜欢数
        TextView commitNum; //不喜欢数

        Boolean isLiked = false;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_bg);
            username = itemView.findViewById(R.id.tv_username);
            userLevel = itemView.findViewById(R.id.tv_level);
            publishTime = itemView.findViewById(R.id.tv_time);
            noteImg = itemView.findViewById(R.id.list_item_icon);
            noteContent = itemView.findViewById(R.id.list_item_content);
            noteFlag = itemView.findViewById(R.id.news_flag);
            likeIcon = itemView.findViewById(R.id.news_item_like);
            likeNum = itemView.findViewById(R.id.news_item_like_num);
            commitNum = itemView.findViewById(R.id.news_item_commit_num);
        }
    }

}
