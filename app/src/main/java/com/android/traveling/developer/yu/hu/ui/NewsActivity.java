package com.android.traveling.developer.yu.hu.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.CommentAdaptor;
import com.android.traveling.developer.zhiming.li.ui.PersonalActivity;
import com.android.traveling.entity.msg.CommentMsg;
import com.android.traveling.entity.note.Comment;
import com.android.traveling.entity.note.CommentService;
import com.android.traveling.entity.note.Note;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.ui
 * 文件名：NewsActivity
 * 创建者：HY
 * 创建时间：2018/9/27 15:43
 * 描述：  游记/攻略详情页面
 */

public class NewsActivity extends BackableActivity {

    //intent传递参数key值

    public static final String s_NOTE = "note";
    public static final String POSITION = "position";
    public static final int RESULT_CODE = 1;   //返回到RecommendFragment
    private static final String TAG = "NewsActivity";
    private int position;

    boolean isLiked = false;
    Note note;

    ScrollView scrollView_news;
    CircleImageView user_bg;
    TextView tv_username;
    TextView tv_time;
    TextView tv_level;
    TextView note_content;
    TextView all_comment_num;
    ImageView note_img;
    RecyclerView recyclerView;
    SmartRefreshLayout refreshLayout;

    TextView tv_like;
    TextView tv_comment;

    ConstraintLayout constraintLayout;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initView();
        initDate();
        //加载评论
        loadComments(note.getId());

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
        tv_like.setText(getResources().getString(R.string.num,note.getLikeNum()));
        tv_comment.setText(getResources().getString(R.string.num,note.getCommentNum()));
        LogUtil.d(TAG, "list=" + note.getStrLikeList());
        if (note.isLiked()) {
            isLiked = true;
            tv_like.setCompoundDrawablesWithIntrinsicBounds(null,
                    ContextCompat.getDrawable(this, R.drawable.ic_like2), null, null);
        } else {
            tv_like.setCompoundDrawablesWithIntrinsicBounds(null,
                    ContextCompat.getDrawable(this, R.drawable.ic_like), null, null);
        }
    }

    //添加事件
    private void addEvents(Note note) {
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
                    UtilTools.toast(NewsActivity.this, "登录之后才可点赞");
                }
            }

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
        constraintLayout = findViewById(R.id.layout_user);
    }

    /**
     * 加载评论
     *
     * @param noteId 文章id
     */
    private void loadComments(int noteId) {
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取NoteService对象
        CommentService commentService = retrofit.create(CommentService.class);
        Call<CommentMsg> msgCall = commentService.getComments(noteId);
        msgCall.enqueue(new Callback<CommentMsg>() {
            @Override
            public void onResponse(@NonNull Call<CommentMsg> call, @NonNull Response<CommentMsg> response) {
                CommentMsg msg = response.body();
                LogUtil.d(new Gson().toJson(msg));
                try {
                    if (msg != null) {
                        if (msg.getComments() == null || msg.getComments().size() == 0) {
                            loadFailure(msg.getInfo());
                        } else {
                            //加载成功
                            LogUtil.d("size=" + msg.getComments().size());
                            loadSuccess(msg.getComments());
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentMsg> call, @NonNull Throwable t) {
                LogUtil.e("LoadMore onFailure t=" + t);
                loadFailure(t.getMessage());
            }
        });
    }

    /**
     * 评论加载失败
     *
     * @param reason reason
     */
    private void loadFailure(String reason) {
        UtilTools.toast(NewsActivity.this, reason);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * 评论加载成功
     *
     * @param comments comments
     */
    private void loadSuccess(List<Comment> comments) {
        all_comment_num.setText(getResources().getString(R.string.news_comment, comments.size()));
        //设置布局方式
        recyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        //setHasFixedSize(true)方法使得RecyclerView能够固定自身size不受adapter变化的影响；
        recyclerView.setHasFixedSize(true);
        //关闭RecyclerView的嵌套滑动特性
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new CommentAdaptor(NewsActivity.this, comments));
        //滚动到顶部
        scrollView_news.scrollTo(0, 0);
    }

    /**
     * 重写方法以将结果返回回去
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(POSITION, position);
        intent.putExtra(s_NOTE, note);
        setResult(RESULT_CODE, intent);
        finish();
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
}
