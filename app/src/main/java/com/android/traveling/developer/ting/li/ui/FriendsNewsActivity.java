package com.android.traveling.developer.ting.li.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.ting.li.adaptor.FriendsReplyAdaptor;
import com.android.traveling.developer.zhiming.li.ui.PersonalActivity;
import com.android.traveling.entity.comment.BaseComment;
import com.android.traveling.entity.comment.Reply;
import com.android.traveling.entity.companion.Companion;
import com.android.traveling.entity.leancloud.CustomUserProvider;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.ReplyDialog;
import com.android.traveling.widget.dialog.ToLoginDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.ting.li.ui
 * 文件名：FriendsNewsActivity
 * 创建者：LT
 * 创建时间：2018/10/4 17:22
 * 描述：  结伴消息详情页面
 */
public class FriendsNewsActivity extends BackableActivity implements FriendsReplyAdaptor.DataChangeListener {
    public static final String s_COMPANION = "s_companion";
    public static final String POSITION = "position";
    private Companion companion;

    ImageView imageView;
    TextView textViewNickName;
    TextView textViewCreateTime;
    TextView textViewContent;
    TextView textViewStartTime;
    TextView textViewEndTime;
    TextView textViewPlace;
    TextView textViewViews;
    TextView tv_write_comment;

    List<Reply> replyList;
    RecyclerView recyclerView;
    FriendsReplyAdaptor friendsReplyAdaptor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsnews_detail);
        initView();
        initDate();
    }

    private void initView() {
        imageView = findViewById(R.id.friends_news_img_detail);
        textViewNickName = findViewById(R.id.friends_news_username_detail);
        textViewCreateTime = findViewById(R.id.friends_news_create_time_detail);
        textViewContent = findViewById(R.id.friends_news_content_detail);
        textViewStartTime = findViewById(R.id.friends_news_start_time_detail);
        textViewEndTime = findViewById(R.id.friends_news_end_time_detail);
        textViewPlace = findViewById(R.id.friends_news_place_detail);
        textViewViews = findViewById(R.id.friends_news_views_detail);
        recyclerView = findViewById(R.id.recycle_view_comment_friends);
        tv_write_comment = findViewById(R.id.tv_write_comment);

    }

    //初始化数据
    @SuppressLint("SetTextI18n")
    private void initDate() {
        Intent intent = getIntent();
        //id = intent.getIntExtra(ID, 0);
        //        int position = intent.getIntExtra(POSITION, -1);
        companion = (Companion) intent.getSerializableExtra(s_COMPANION);
        setTitle(companion.getTitle());  //设置标题
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        if (companion.getImgUrl() != null) {
            Picasso.get().load(companion.getImgUrl())
                    .placeholder(R.drawable.err_img_bg)
                    .error(R.drawable.err_img_bg).fit()
                    .into(imageView);
        }
        if (companion.getNickName() != null) {
            textViewNickName.setText(companion.getNickName());
        } else {
            textViewNickName.setText(R.string.list_friends_item_username);
        }
        if (companion.getCreateTime() != null) {
            textViewCreateTime.setText(format.format(companion.getCreateTime()));
        }
        if (companion.getContent() != null) {
            textViewContent.setText(companion.getContent());
        }
        if (companion.getStartTime() != null) {
            textViewStartTime.setText(format.format(companion.getStartTime()));
        }
        if (companion.getEndTime() != null) {
            textViewEndTime.setText(format.format(companion.getEndTime()));
        }
        if (companion.getTarget() != null) {
            textViewPlace.setText(companion.getTarget());
        }
        if (companion.getViews() != null) {
            textViewViews.setText(companion.getViews().toString() + "浏览");
        }
        LogUtil.d("44" + companion.getId());
        //replyList = comment.getReplies();
        Companion.getCompanionComments(companion.getId(), new Companion.Callback3() {
            @Override
            public void onSuccess(List<Reply> replies) {
                replyList = replies;
                //设置布局方式
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendsNewsActivity.this));
                //关闭RecyclerView的嵌套滑动特性
                //recyclerView.setNestedScrollingEnabled(false);
                friendsReplyAdaptor = new FriendsReplyAdaptor(FriendsNewsActivity.this, companion.getId(), replyList);
                recyclerView.setAdapter(friendsReplyAdaptor);
                friendsReplyAdaptor.notifyDataSetChanged();
                UtilTools.toast(FriendsNewsActivity.this, "加载成功:" + replyList.size());
            }

            @Override
            public void onFailure(int errCode, String reason) {
                if (errCode == Msg.NO_DATA) {
                    //refreshLayout.finishRefresh(false);
                    UtilTools.toast(FriendsNewsActivity.this, "加载失败:" + reason);
                } else {
                    UtilTools.toast(FriendsNewsActivity.this, "加载失败:" + errCode);
                }
            }
        });

        //滚动到顶部
        //scrollView_news.scrollTo(0, 0);
        //私聊点击按钮
        findViewById(R.id.friendsnews_detail_chat).setOnClickListener(v -> {
            User currentUser1 = TravelingUser.getCurrentUser();
            if (currentUser1 == null) {
                new ToLoginDialog(FriendsNewsActivity.this, "登录之后才可以开始私聊哦").show();
                return;
            }
            //LogUtil.d("与" + userId + "开始聊天");
            LogUtil.d("666与" + companion.getUserId() + "开始聊天");
            try {
                // 点击联系人，直接跳转进入聊天界面
                Intent intent1 = new Intent(FriendsNewsActivity.this, LCIMConversationActivity.class);
                // 传入对方的 Id 即可
                intent1.putExtra(LCIMConstants.PEER_ID, String.valueOf(companion.getUserId()));
                CustomUserProvider.addUser(currentUser1);
                startActivity(intent1);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d("LeanCloud开启聊天异常");
            }

        });
        //用户点击事件
        findViewById(R.id.friends_news_img_detail).setOnClickListener(v -> {
            Intent toPersonal = new Intent(this, PersonalActivity.class);
            toPersonal.putExtra(PersonalActivity.USER_ID, companion.getUserId());
            startActivity(toPersonal);
        });

        //评论
        tv_write_comment.setOnClickListener(v -> {
            User currentUser = TravelingUser.checkLogin(this);
            if (currentUser == null) {
                return;
            }
            new ReplyDialog(this, (v1, content) -> {
                BaseComment baseComment = new BaseComment(companion.getId(), currentUser.getUserId(), content);
                baseComment.setFlag(Reply.FLAG_COMPANION_COMMENT);
                LogUtil.d("baseComment=" + baseComment);
                BaseComment.addComment(this, baseComment, new BaseComment.AddCommentListener() {
                    @Override
                    public void onSuccess(BaseComment baseComment1) {
                        addCommentSuccess(currentUser, baseComment1, companion);
                    }

                    @Override
                    public void onFailure(String reason) {
                        UtilTools.toast(FriendsNewsActivity.this, "2发布失败：" + reason);
                    }
                });
            }).show();
        });
    }

    /**
     * 评论成功后UI界面的一些操作
     *
     * @param currentUser 当前登录的用户
     * @param baseComment baseComment
     * @param companion   companion
     */
    private void addCommentSuccess(User currentUser, BaseComment baseComment, Companion companion) {
        if (replyList == null || replyList.size() == 0) {
            replyList = new ArrayList<>();
            friendsReplyAdaptor = new FriendsReplyAdaptor(FriendsNewsActivity.this, companion.getId(), replyList);
            recyclerView.setAdapter(friendsReplyAdaptor);
            recyclerView.setVisibility(View.VISIBLE);
            //refreshLayout.setEnableLoadMore(true);
        }
        Reply rr = new Reply(currentUser, baseComment);
        replyList.add(0, new Reply(currentUser, baseComment));
        LogUtil.d(replyList.get(0).getContent() + replyList.get(0).getUserImg());
        LogUtil.d("889" + rr.getContent() + rr.getUserImg());
        LogUtil.d("889" + rr.getContent() + currentUser.getDirectImg() + currentUser.getImg() + currentUser.getNickName());
        friendsReplyAdaptor.notifyItemInserted(0);
        recyclerView.getAdapter().notifyItemRangeChanged(0, replyList.size());
        //companion.setCommentNum(replyList.size());
        //tv_comment.setText(String.valueOf(replyList.size()));
        //all_comment_num.setText(getResources().getString(R.string.news_comment, replyList.size()));
    }

    @Override
    public void onDataChanged(List<Reply> replies) {
    }
}
