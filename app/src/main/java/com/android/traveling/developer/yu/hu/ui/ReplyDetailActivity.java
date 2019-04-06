package com.android.traveling.developer.yu.hu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.ReplyAdapter;
import com.android.traveling.developer.zhiming.li.ui.PersonalActivity;
import com.android.traveling.entity.comment.BaseComment;
import com.android.traveling.entity.comment.Comment;
import com.android.traveling.entity.comment.Reply;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.ReplyDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.ui
 * 文件名：ReplyDetailActivity
 * 创建者：HY
 * 创建时间：2019/2/25 23:30
 * 描述：  评论详情
 */

public class ReplyDetailActivity extends BackableActivity implements ReplyAdapter.DataChangeListener {

    public static final String NOTE_ID = "noteId";
    public static final String COMMENT = "comment";
    public static final int RESULT_CODE = 1;   //返回到NesActivity

    Comment comment;
    List<Reply> replyList;
    int noteId;
    int position;

    ScrollView scrollView_news;
    CircleImageView user_bg;
    TextView tv_username;
    TextView tv_time;
    TextView tv_content;
    TextView tv_reply;
    TextView tv_reply_num;
    RecyclerView recyclerView;
    ReplyAdapter replyAdapter;

    TextView tv_comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_detail);
        initView();
        setTitle("回复详情");
        Intent intent = getIntent();
        noteId = intent.getIntExtra(NOTE_ID, 0);
        position = intent.getIntExtra(NewsActivity.POSITION, 0);
        comment = (Comment) intent.getSerializableExtra(COMMENT);
        initDate();
        addEvent();
    }

    //初始化控件
    private void initView() {
        scrollView_news = findViewById(R.id.scrollView_news);
        user_bg = findViewById(R.id.user_img);
        tv_username = findViewById(R.id.tv_user_name);
        tv_content = findViewById(R.id.tv_content);
        tv_time = findViewById(R.id.tv_comment_time);
        tv_reply = findViewById(R.id.tv_reply);
        tv_reply_num = findViewById(R.id.reply_num);
        recyclerView = findViewById(R.id.recycle_view);
        tv_comment = findViewById(R.id.tv_comment);
    }

    //初始化数据
    private void initDate() {
        Picasso.get().load(comment.getUserImg()).into(user_bg);
        tv_username.setText(comment.getNickName());
        tv_content.setText(comment.getContent());
        tv_time.setText(DateUtil.fromNow(comment.getCommentTime()));
        tv_reply.setVisibility(View.INVISIBLE);
        tv_reply_num.setText(getResources().getString(R.string.comments, comment.getReplies().size()));
        tv_comment.setHint(comment.getNickName());
        replyList = comment.getReplies();

        //设置布局方式
        recyclerView.setLayoutManager(new LinearLayoutManager(ReplyDetailActivity.this));
        //关闭RecyclerView的嵌套滑动特性
        recyclerView.setNestedScrollingEnabled(false);
        replyAdapter = new ReplyAdapter(ReplyDetailActivity.this, noteId, comment.getId(),replyList);
        recyclerView.setAdapter(replyAdapter);
        //滚动到顶部
        scrollView_news.scrollTo(0, 0);
    }

    //添加事件
    private void addEvent() {
        // 头像/用户名点击事件
        user_bg.setOnClickListener(v -> {
            UtilTools.toast(this, "点击了" + comment.getNickName() + "的信息");
            Intent intent = new Intent(this, PersonalActivity.class);
            intent.putExtra(PersonalActivity.USER_ID, comment.getUserId());
            startActivity(intent);
        });
        tv_username.setOnClickListener(v -> user_bg.callOnClick());

        // 回复点击事件
        tv_comment.setOnClickListener(v -> {
            User currentUser = TravelingUser.checkLogin(this);
            if (currentUser == null) {
                return;
            }
            String hint = getResources().getString(R.string.str_reply_to, comment.getNickName());
            new ReplyDialog(this, hint, (v1, content) -> {
                BaseComment baseComment = new BaseComment(Reply.FLAG_COMMENT, noteId,
                        comment.getId(), currentUser.getUserId(), content
                );
                BaseComment.addComment(this, baseComment, new BaseComment.AddCommentListener() {
                    @Override
                    public void onSuccess(BaseComment baseComment) {
                        LogUtil.d("position= " + position);
                        comment.addReply(currentUser, baseComment);
                        replyList = comment.getReplies();
                        tv_reply_num.setText(getResources().getString(R.string.comments, comment.getReplies().size()));
                        replyAdapter.notifyItemInserted(replyList.size() - 1);
                        recyclerView.getAdapter().notifyItemChanged(replyList.size() - 1);
                        UtilTools.toast(ReplyDetailActivity.this, "发表成功");

                    }

                    @Override
                    public void onFailure(String reason) {
                        UtilTools.toast(ReplyDetailActivity.this, "发表失败：" + reason);
                    }
                });

            }).show();
        });
    }

    @Override
    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(NewsActivity.POSITION, position);
        intent.putExtra(COMMENT, comment);
        setResult(RESULT_CODE, intent);
        super.onBack();
    }

    @Override
    public void onDataChanged(List<Reply> replies) {
        this.replyList = replies;
        tv_reply_num.setText(getResources().getString(R.string.comments, comment.getReplies().size()));
    }
}
