package com.yu.hu.traveling.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yu.hu.library.widget.ICustomView;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.entity.note.Note;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.widget
 * 文件名：CommentBar
 * 创建者：HY
 * 创建时间：2019/7/3 13:19
 * 描述：  底部评论那一行
 * <p>
 * {@code  R.layout.activity_note_detail.xml}
 */
public class CommentBar extends LinearLayout implements ICustomView {

    private Context mContext;

    private TextView mWriteCommentBtn, mLikeTV, mCommentTV;

    private OnPublishClickListener mListener;

    /**
     * 模式
     * {@link #build(String)}
     * {@link #build(Note)}
     */
    //todo  不确定是否有用
    private int mode;

    //当前所显示的 是否喜欢 的状态
    private boolean isLiked;

    public CommentBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_comment_bar, this, true);
        initView(context);
    }

    @Override
    public void initView(Context context) {
        setOrientation(HORIZONTAL);

        mWriteCommentBtn = findViewById(R.id.tv_write_comment);
        mLikeTV = findViewById(R.id.tv_like);
        mCommentTV = findViewById(R.id.tv_comment);
        mWriteCommentBtn.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick();
            }
        });
    }

    /**
     * 发表评论点击事件
     */
    public void setOnPublishClickListener(OnPublishClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 显示在评论详情页面里
     *
     * @param toName 评论谁（名字）
     * @see com.yu.hu.traveling.activity.ReplyDetailActivity
     */
    public void build(String toName) {
        mode = 0;
        mWriteCommentBtn.setHint(toName);
        mLikeTV.setVisibility(GONE);
        mCommentTV.setVisibility(GONE);
    }

    /**
     * 显示在游记详情页面中
     *
     * @see com.yu.hu.traveling.activity.NoteDetailActivity
     */
    public void build(Note note) {
        mode = 1;
        mWriteCommentBtn.setHint(mContext.getString(R.string.write_comments));
        mLikeTV.setText(mContext.getString(R.string.num, note.getLikeNum()));
        mCommentTV.setText(mContext.getString(R.string.num, note.getCommentNum()));

        if (note.isLiked()) {
            isLiked = true;
            mLikeTV.setCompoundDrawablesWithIntrinsicBounds(null,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_like2), null, null);
        } else {
            isLiked = false;
            mLikeTV.setCompoundDrawablesWithIntrinsicBounds(null,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_like), null, null);
        }
    }

    public interface OnPublishClickListener {
        void onClick();
    }
}
