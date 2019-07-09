package com.yu.hu.traveling.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yu.hu.library.util.DateUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.activity.NoteDetailActivity;
import com.yu.hu.traveling.activity.ReplyDetailActivity;
import com.yu.hu.traveling.entity.user.TravelingUser;
import com.yu.hu.traveling.entity.user.User;
import com.yu.hu.traveling.entity.comment.Comment;
import com.yu.hu.traveling.entity.note.Note;
import com.yu.hu.traveling.holder.CommentViewHolder;
import com.yu.hu.traveling.holder.LoadingHolder;
import com.yu.hu.traveling.util.GlideUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.adapter
 * 文件名：NoteDetailAdapter
 * 创建者：HY
 * 创建时间：2019/7/2 17:58
 * 描述：  游记详细内容
 *
 * @see com.yu.hu.traveling.activity.NoteDetailActivity
 */
public class NoteDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 头部游记详细信息的标记位
     */
    private static final int VIEW_TYPE_HEAD = 0;
    /**
     * 评论的标记位
     */
    private static final int VIEW_TYPE_COMMENT = 1;


    private Context mContext;

    private Note mNote;

    private List<Comment> mCommentList;

    public NoteDetailAdapter(Context context, Note note) {
        this.mNote = note;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //顶部游记详细信息
            return VIEW_TYPE_HEAD;
        }
        if (position == 1 && mCommentList == null) {
            //加载中进度条
            return Integer.MAX_VALUE;
        }
        //评论
        return VIEW_TYPE_COMMENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_HEAD:
                view = layoutInflater.inflate(R.layout.item_note_detail_top, parent, false);
                return new NoteDetailViewHolder(view);
            case VIEW_TYPE_COMMENT:
                view = layoutInflater.inflate(R.layout.item_comment, parent, false);
                return new CommentViewHolder(view);
            default:
                view = layoutInflater.inflate(R.layout.item_loading, parent, false);
                return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //顶部游记详细信息
        if (holder instanceof NoteDetailViewHolder) {
            NoteDetailViewHolder noteDetailViewHolder = (NoteDetailViewHolder) holder;
            //初始化viewHolder
            initNoteDetailHolder(noteDetailViewHolder);
            return;
        }

        //评论
        if (holder instanceof CommentViewHolder) {
            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            //初始化viewHolder
            initCommentHolder(commentViewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mCommentList == null) {
            return 2;
        }
        return mCommentList.size() + 1;
    }

    //设置评论
    public void setComments(List<Comment> comments) {
        this.mCommentList = comments;
        notifyDataSetChanged();
    }

    /**
     * 初始化 游记详情 对应的 ViewHolder
     */
    private void initNoteDetailHolder(NoteDetailViewHolder noteDetailViewHolder) {
        GlideUtil.loadWithoutAnimate(mContext, mNote.getReleasePeople().getImgUrl())
                .into(noteDetailViewHolder.userImg);

        if (mNote.getImgList().size() != 0) {
            noteDetailViewHolder.noteImg.setVisibility(View.VISIBLE);
            GlideUtil.load(mContext, mNote.getImgList().get(0))
                    .into(noteDetailViewHolder.noteImg);
        } else {
            noteDetailViewHolder.noteImg.setVisibility(View.GONE);
        }

        noteDetailViewHolder.username.setText(mNote.getReleasePeople().getNickName());
        noteDetailViewHolder.publishTime.setText(DateUtil.fromNow(mNote.getCreateTime()));
        noteDetailViewHolder.userLevel.setText(String.format(mContext.getString(R.string.level), "LV."
                , mNote.getReleasePeople().getLevel()));
        noteDetailViewHolder.noteContent.setText(mNote.getContent());

        //评论数
        int commentNum = mCommentList == null ? 0 : mCommentList.size();
        noteDetailViewHolder.commentNum.setText(commentNum == 0 ? mContext.getString(R.string.no_comments) :
                mContext.getString(R.string.note_comments, commentNum));
    }

    /**
     * 初始化 评论 对应的 ViewHolder
     *
     * @param commentViewHolder viewHolder
     * @param position          这条评论在recyclerView中的位置
     */
    private void initCommentHolder(CommentViewHolder commentViewHolder, int position) {
        Comment comment = mCommentList.get(position - 1);

        GlideUtil.loadWithoutAnimate(mContext, comment.getUserImg())
                .into(commentViewHolder.userImg);

        commentViewHolder.username.setText(comment.getNickName());
        commentViewHolder.content.setText(comment.getContent());
        commentViewHolder.commentTime.setText(DateUtil.fromNow(comment.getCommentTime()));
        commentViewHolder.replyBox.build(comment.getReplies());
        commentViewHolder.replyBox.setOnClickListener(v -> {
            //todo
            Intent intent = new Intent(mContext, ReplyDetailActivity.class);
            intent.putExtra(ReplyDetailActivity.NOTE_ID, mNote.getId());
            intent.putExtra(NoteDetailActivity.POSITION, position);
            intent.putExtra(ReplyDetailActivity.COMMENT, comment);
            ((NoteDetailActivity) mContext).startActivityForResult(intent, NoteDetailActivity.REQUEST_CODE);
        });
        commentViewHolder.replyBtn.setOnClickListener(v -> {
            //todo
        });

        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null && currentUser.getId().equals(Long.valueOf(comment.getUserId()))) {
            commentViewHolder.deleteBtn.setVisibility(View.VISIBLE);
            commentViewHolder.deleteBtn.setOnClickListener(v -> {
                //todo
            });
        } else {
            commentViewHolder.deleteBtn.setVisibility(View.GONE);
        }
    }

    /**
     * {@link ReplyDetailActivity}返回的 Comment的可能会增加有新的评论
     */
    public void onActivityResult(int position, Comment comment) {
        //这个position是在recyclerView中所显示的位置
        //因为有一个头部是显示Note详情的，所以在数据列表中的位置应该减1才对
        mCommentList.set(position - 1, comment);
        notifyItemChanged(position);
    }


    //顶部游记详细信息
    class NoteDetailViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImg; //用户头像
        TextView username; //用户名
        TextView userLevel; //等级
        TextView publishTime; //发布时间

        ImageView noteImg; //游记图片
        TextView noteContent; //游记内容
        TextView commentNum; //评论数

        NoteDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_bg);
            username = itemView.findViewById(R.id.tv_username);
            userLevel = itemView.findViewById(R.id.tv_level);
            publishTime = itemView.findViewById(R.id.tv_time);
            noteImg = itemView.findViewById(R.id.note_img);
            noteContent = itemView.findViewById(R.id.note_content);
            commentNum = itemView.findViewById(R.id.all_comment_num);
        }
    }
}
