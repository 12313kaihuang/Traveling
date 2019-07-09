package com.yu.hu.traveling.activity;

import android.content.Intent;
import android.os.Bundle;

import com.yu.hu.library.activity.BackableActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.adapter.ReplyDetailAdapter;
import com.yu.hu.traveling.entity.comment.Comment;
import com.yu.hu.traveling.widget.CommentBar;
import com.yu.hu.traveling.widget.ReplyDialog;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.activity
 * 文件名：ReplyDetailActivity
 * 创建者：HY
 * 创建时间：2019/7/3 14:06
 * 描述：  回复详情
 */
public class ReplyDetailActivity extends BackableActivity {

    public static final String NOTE_ID = "mNoteId";
    public static final String COMMENT = "mComment";
    public static final int RESULT_CODE = 1;   //返回到NoteDetailActivity

    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.comment_bar)
    CommentBar mCommentBar;

    private int mNoteId;
    private Comment mComment;
    private int mCommentPosition;

    private ReplyDetailAdapter mAdapter;

    private ReplyDialog mPublishDialog;

    @Override
    protected int getLayoutId() {
        //与 NoteDetailActivity可以使用用一个布局文件
        return R.layout.activity_reclcler_with_comment_bar;
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        setTitle("回复详情");
        Intent intent = getIntent();
        mNoteId = intent.getIntExtra(NOTE_ID, 0);
        mCommentPosition = intent.getIntExtra(NoteDetailActivity.POSITION, 0);
        mComment = (Comment) intent.getSerializableExtra(COMMENT);

        mAdapter = new ReplyDetailAdapter(this, mComment);
        //设置布局方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //添加动画效果
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mCommentBar.build(mComment.getNickName());
        mCommentBar.setOnPublishClickListener(this::showPublishDialog);
    }

    /**
     * 发表回复
     */
    private void showPublishDialog() {
        if (mPublishDialog == null) {
            mPublishDialog =  new ReplyDialog.Builder(ReplyDetailActivity.this)
                    .setHint(getString(R.string.reply_to, mComment.getNickName()))
                    .setOnPublishListener((v, content) -> {
                        addComment(content);
                    })
                    .build();
        }
        mPublishDialog.show();
    }

    /**
     * 添加评论
     */
    private void addComment(String content) {
        Intent intent = new Intent();
        intent.setAction("");
    }

    @Override
    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(NoteDetailActivity.POSITION, mCommentPosition);
        intent.putExtra(COMMENT, mComment);
        setResult(RESULT_CODE, intent);
        super.onBack();
    }
}
