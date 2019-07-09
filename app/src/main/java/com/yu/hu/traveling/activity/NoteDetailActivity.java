package com.yu.hu.traveling.activity;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.library.activity.BackableActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.adapter.NoteDetailAdapter;
import com.yu.hu.traveling.entity.comment.Comment;
import com.yu.hu.traveling.entity.exception.TravelingRuntimeException;
import com.yu.hu.traveling.entity.note.Note;
import com.yu.hu.traveling.mvp.impl.NoteDetailPrensence;
import com.yu.hu.traveling.mvp.impl.NoteDetailPresenter;
import com.yu.hu.traveling.widget.CommentBar;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.activity
 * 文件名：NoteDetailActivity
 * 创建者：HY
 * 创建时间：2019/7/2 17:11
 * 描述：  游记详情页
 */
public class NoteDetailActivity extends BackableActivity<NoteDetailPrensence, NoteDetailPresenter>
        implements NoteDetailPrensence {

    //intent传递参数key值
    public static final String s_NOTE = "mNote";
    public static final String POSITION = "mNotePosition";
    public static final int REQUEST_CODE = 0;  //请求码 去ReplyDetailActivity
    public static final int RESULT_CODE = 1;   //返回到RecommendFragment
    private static final String TAG = "NoteDetailActivity";

    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.comment_bar)
    CommentBar mCommentBar;

    private NoteDetailAdapter mAdapter;

    //note在列表中的位置
    private int mNotePosition;
    Note mNote;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reclcler_with_comment_bar;
    }

    @Override
    protected NoteDetailPresenter attachPresenter() {
        return new NoteDetailPresenter(this);
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        //初始化操作
        init();
        //加载评论
        loadComments();
    }

    /**
     * 初始化
     */
    private void init() {
        Intent intent = getIntent();
        mNotePosition = intent.getIntExtra(POSITION, -1);
        mNote = (Note) intent.getSerializableExtra(s_NOTE);
        if (mNote == null) {
            throw new TravelingRuntimeException("NoteDetailActivity -- 没有传递Note进来");
        }
        setTitle(mNote.getTitle());  //设置标题

        mAdapter = new NoteDetailAdapter(this, mNote);
        //设置布局方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //添加动画效果
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mCommentBar.build(mNote);
    }

    /**
     * 加载文章评论
     */
    private void loadComments() {
        mPresenter.loadComments(mNote.getId());
    }

    @Override
    public void onLoadComments(List<Comment> comments) {
        if (comments.size() == 0) {
            ToastUtils.showShort("暂无评论");
        }
        mAdapter.setComments(comments);
    }

    @Override
    public void onLoadError(Throwable e) {
        ToastUtils.showShort("评论加载失败：" + e.getMessage());
        //todo adapter
    }

    /**
     * 重写方法以将结果返回回去
     * 按下返回键触发(上部返回键或底部返回键均会触发)
     */
    @Override
    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(POSITION, mNotePosition);
        intent.putExtra(s_NOTE, mNote);
        setResult(RESULT_CODE, intent);
        super.onBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ReplyDetailActivity
        if (requestCode == REQUEST_CODE && resultCode == ReplyDetailActivity.RESULT_CODE) {
            int position = data.getIntExtra(POSITION, 0);
            Comment comment = (Comment) data.getSerializableExtra(ReplyDetailActivity.COMMENT);
            mAdapter.onActivityResult(position, comment);
        }
    }
}
