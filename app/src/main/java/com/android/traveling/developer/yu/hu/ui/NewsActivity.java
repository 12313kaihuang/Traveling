package com.android.traveling.developer.yu.hu.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.CommentAdapter;
import com.android.traveling.developer.zhiming.li.ui.PersonalActivity;
import com.android.traveling.entity.comment.BaseComment;
import com.android.traveling.entity.comment.Comment;
import com.android.traveling.entity.note.Note;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.ReplyDialog;
import com.android.traveling.widget.ToLoginDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.ui
 * 文件名：NewsActivity
 * 创建者：HY
 * 创建时间：2018/9/27 15:43
 * 描述：  游记/攻略详情页面
 */

public class NewsActivity extends BackableActivity implements CommentAdapter.DataChangeListener {

    //intent传递参数key值
    public static final String s_NOTE = "note";
    public static final String POSITION = "position";
    public static final int REQUEST_CODE = 0;  //请求码 去ReplyDetailActivity
    public static final int RESULT_CODE = 1;   //返回到RecommendFragment
    private static final String TAG = "NewsActivity";
    //note在列表中的位置
    private int position;

    boolean isLiked = false;
    Note note;

    ScrollView scrollView_news;
    CircleImageView user_bg;
    TextView tv_username;
    TextView tv_time;
    TextView tv_level;
    TextView note_content;
    //最新评论
    TextView all_comment_num;
    ImageView note_img;
    RecyclerView recyclerView;
    SmartRefreshLayout refreshLayout;

    TextView tv_like;
    //评论栏里的评论TextView
    TextView tv_comment;
    TextView tv_write_comment;

    ConstraintLayout constraintLayout;

    List<Comment> comments = null;
    CommentAdapter commentAdapter = null;

    ToLoginDialog toLoginDialog;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initView();
        initDate();
        //加载评论
        loadComments();

    }

    //初始化数据
    private void initDate() {
        Intent intent = getIntent();
        position = intent.getIntExtra(POSITION, -1);
        note = (Note) intent.getSerializableExtra(s_NOTE);
        setTitle(note.getTitle());  //设置标题
        addEvents(note);

        //头像
        Picasso.get().load(note.getReleasePeople().getImgUrl()).into(user_bg);
        Picasso.get().load(note.getImgList().get(0)).into(note_img);
        tv_username.setText(note.getReleasePeople().getNickName());
        tv_time.setText(DateUtil.fromNow(note.getCreateTime()));
        tv_level.setText(String.format(getString(R.string.level), "LV."
                , note.getReleasePeople().getLevel()));
        note_content.setText(note.getContent());
        tv_like.setText(getResources().getString(R.string.num, note.getLikeNum()));
        tv_comment.setText(getResources().getString(R.string.num, note.getCommentNum()));
        if (note.isLiked()) {
            isLiked = true;
            tv_like.setCompoundDrawablesWithIntrinsicBounds(null,
                    ContextCompat.getDrawable(this, R.drawable.ic_like2), null, null);
        } else {
            tv_like.setCompoundDrawablesWithIntrinsicBounds(null,
                    ContextCompat.getDrawable(this, R.drawable.ic_like), null, null);
        }
    }

    /**
     * 添加事件
     *
     * @param note note
     */
    private void addEvents(Note note) {
        refreshLayout.setOnLoadMoreListener(refreshLayout -> note.loadComments(new Note.Callback2() {
            @Override
            public void onSuccess(List<Comment> commentList) {
                comments = commentList;
                if (commentAdapter == null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    refreshLayout.setEnableLoadMore(true);
                }
                tv_comment.setText(String.valueOf(comments.size()));
                all_comment_num.setText(getResources().getString(R.string.news_comment, comments.size()));
                commentAdapter = new CommentAdapter(NewsActivity.this, note.getId(), comments);
                recyclerView.setAdapter(commentAdapter);
                commentAdapter.notifyItemInserted(0);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onFailure(String reason) {
                refreshLayout.finishLoadMore(false);
            }
        }));

        //用户点击事件
        constraintLayout.setOnClickListener(v -> {
            Intent toPersonal = new Intent(this, PersonalActivity.class);
            toPersonal.putExtra(PersonalActivity.USER_ID, note.getUserId());
            startActivity(toPersonal);
        });


        //点赞事件
        tv_like.setOnClickListener(v -> {

            int num = Integer.parseInt((String) tv_like.getText());
            if (note.isLiked()) {
                tv_like.setText(getResources().getString(R.string.num, num - 1));
                tv_like.setCompoundDrawablesWithIntrinsicBounds(null,
                        ContextCompat.getDrawable(this, R.drawable.ic_like), null, null);
                note.doLike(Note.DISLIKE);
            } else {
                if (TravelingUser.hasLogin()) {
                    tv_like.setText(getResources().getString(R.string.num, num + 1));
                    tv_like.setCompoundDrawablesWithIntrinsicBounds(null,
                            ContextCompat.getDrawable(this, R.drawable.ic_like2), null, null);
                    UtilTools.showGoodView(v, this);
                    note.doLike(Note.LIKE);
                } else {
                    if (toLoginDialog == null) {
                        toLoginDialog = new ToLoginDialog(this, "登录之后才可点赞哦");
                    }
                    toLoginDialog.show();
                }
            }

        });

        //评论
        tv_write_comment.setOnClickListener(v -> {
            User currentUser = TravelingUser.checkLogin(this);
            if (currentUser == null) {
                return;
            }
            new ReplyDialog(this, (v1, content) -> {

                BaseComment baseComment = new BaseComment(note.getId(), currentUser.getUserId(), content);
                baseComment.setFlag(Comment.FLAG_COMMENT);
                LogUtil.d("baseComment=" + baseComment);
                BaseComment.addComment(this, baseComment, new BaseComment.AddCommentListener() {
                    @Override
                    public void onSuccess(BaseComment baseComment1) {
                        addCommentSuccess(currentUser, baseComment1, note);
                    }

                    @Override
                    public void onFailure(String reason) {
                        LogUtil.d("2发布失败：" + reason);
                        UtilTools.toast(NewsActivity.this, "2发布失败：" + reason);
                    }
                });

            }).show();
        });
    }


    private void initView() {
        user_bg = findViewById(R.id.user_bg);
        tv_username = findViewById(R.id.tv_username);
        tv_time = findViewById(R.id.tv_time);
        tv_level = findViewById(R.id.tv_level);
        note_img = findViewById(R.id.note_img);
        note_content = findViewById(R.id.note_content);
        recyclerView = findViewById(R.id.recycle_view);
        scrollView_news = findViewById(R.id.scrollView_news);
        refreshLayout = findViewById(R.id.refreshLayout);
        all_comment_num = findViewById(R.id.all_comment_num);
        tv_like = findViewById(R.id.tv_like);
        tv_comment = findViewById(R.id.tv_comment);
        tv_write_comment = findViewById(R.id.tv_write_comment);
        constraintLayout = findViewById(R.id.layout_user);

        //设置布局方式
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setHasFixedSize(true)方法使得RecyclerView能够固定自身size不受adapter变化的影响；
        //        recyclerView.setHasFixedSize(true);
        //关闭RecyclerView的嵌套滑动特性
        recyclerView.setNestedScrollingEnabled(false);
        //添加动画效果
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * 评论成功后UI界面的一些操作
     *
     * @param currentUser 当前登录的用户
     * @param baseComment baseComment
     * @param note        note
     */
    private void addCommentSuccess(User currentUser, BaseComment baseComment, Note note) {
        if (comments == null || commentAdapter == null) {
            comments = new ArrayList<>();
            commentAdapter = new CommentAdapter(NewsActivity.this, note.getId(), comments);
            recyclerView.setVisibility(View.VISIBLE);
            refreshLayout.setEnableLoadMore(true);
        }
        comments.add(0, new Comment(currentUser, baseComment));
        commentAdapter.notifyItemInserted(0);
        recyclerView.getAdapter().notifyItemRangeChanged(0, comments.size());
        note.setCommentNum(comments.size());
        tv_comment.setText(String.valueOf(comments.size()));
        all_comment_num.setText(getResources().getString(R.string.news_comment, comments.size()));
    }

    /**
     * 加载评论
     */
    private void loadComments() {

        note.loadComments(new Note.Callback2() {
            @Override
            public void onSuccess(List<Comment> commentList) {
                comments = commentList;
                note.setCommentNum(comments.size());
                loadSuccess();
            }

            @Override
            public void onFailure(String reason) {
                loadFailure(reason);
            }
        });
    }

    /**
     * 评论加载失败
     *
     * @param reason reason
     */
    private void loadFailure(String reason) {
        if ("暂无评论".equals(reason)) {
            all_comment_num.setText(reason);
        } else {
            UtilTools.toast(this, "评论加载失败：" + reason);
            LogUtil.d(TAG, "loadFailure: " + reason);
        }
        recyclerView.setVisibility(View.INVISIBLE);
        refreshLayout.setEnableLoadMore(false);
    }

    /**
     * 评论加载成功
     */
    private void loadSuccess() {
        all_comment_num.setText(getResources().getString(R.string.news_comment, comments.size()));
        commentAdapter = new CommentAdapter(this, note.getId(), comments);
        recyclerView.setAdapter(commentAdapter);
        //滚动到顶部
        scrollView_news.scrollTo(0, 0);
    }


    /**
     * 重写方法以将结果返回回去
     * 按下返回键触发(上部返回键或底部返回键均会触发)
     */
    @Override
    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(POSITION, position);
        intent.putExtra(s_NOTE, note);
        setResult(RESULT_CODE, intent);
        super.onBack();
    }

    @Override
    protected void onPause() {
        LogUtil.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * CommentAdapter数据发生变化时的回调接口
     *
     * @param comments comments
     */
    @Override
    public void onDataChanged(List<Comment> comments) {
        this.comments = comments;
        note.setCommentNum(comments.size());
        tv_comment.setText(String.valueOf(comments.size()));
        all_comment_num.setText(getResources().getString(R.string.news_comment, comments.size()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ReplyDetailActivity
        if (requestCode == REQUEST_CODE && resultCode == ReplyDetailActivity.RESULT_CODE) {
            int position = data.getIntExtra(POSITION, 0);
            Comment comment = (Comment) data.getSerializableExtra(ReplyDetailActivity.COMMENT);
            comments.set(position, comment);
            commentAdapter.notifyDataSetChanged();
        }
    }
}
