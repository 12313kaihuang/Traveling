package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.NewsAdapter;
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.entity.note.Note;
import com.android.traveling.entity.user.DetailUserInfo;
import com.android.traveling.entity.user.DetailUserInfoCallback;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.MyActionBar;
import com.android.traveling.widget.dialog.ToLoginDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：PersonalActivity
 * 创建者：HY
 * 创建时间：2019/2/26 7:49
 * 描述：  个人主页
 */

public class PersonalActivity extends AppCompatActivity {

    public static final String USER_ID = "userID";

    //用户详细信息
    TextView focusNum; //关注数
    TextView fansNum; //粉丝数
    TextView collectionsNum;  //获赞与收藏
    ImageView iv_user_bg;
    ImageView user_img;
    ListView listView;
    ScrollView scrollView;

    TextView tv_username;
    TextView tv_focus;  // 关注/已关注 按钮
    MyActionBar myActionBar;
    private List<Note> noteList;
    private NewsAdapter newsAdapter;
    private boolean isFocus = false;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ImmersionBar.with(this).init();

        Intent intent = getIntent();
        userId = intent.getIntExtra(USER_ID, 0);
        if (userId == 0) {
            UtilTools.toast(this, "userID传参有误");
            finish();
        }
        initView();
        addEvents();
        getDetailUserInfo();
    }

    private void initView() {
        focusNum = findViewById(R.id.my_focus_num);
        fansNum = findViewById(R.id.my_fans_num);
        collectionsNum = findViewById(R.id.my_collections_num);
        user_img = findViewById(R.id.user_img);
        tv_username = findViewById(R.id.tv_username);
        iv_user_bg = findViewById(R.id.iv_user_bg);
        tv_focus = findViewById(R.id.tv_focus);
        listView = findViewById(R.id.list_view);
        scrollView = findViewById(R.id.scrollView);
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null && currentUser.getUserId() == userId) {
            findViewById(R.id.ll_focus_or_chat).setVisibility(View.GONE);
        } else {
            //关注点击事件
            tv_focus.setOnClickListener(v -> {
                User user = TravelingUser.getCurrentUser();
                if (user != null) {
                    user.addOrCancelFocus(isFocus, userId);
                } else {
                    new ToLoginDialog(PersonalActivity.this, "登录之后才可以+关注哦").show();
                    return;
                }
                if (!isFocus) {
                    UtilTools.showGoodView(this, tv_focus, "已关注",
                            getResources().getColor(R.color.dialog_orange));
                    isFocus = true;
                } else {
                    isFocus = false;
                }
                setFocus(isFocus);
            });
            //私聊点击按钮
            findViewById(R.id.tv_chat).setOnClickListener(v -> {
                if (TravelingUser.getCurrentUser() == null) {
                    new ToLoginDialog(PersonalActivity.this, "登录之后才可以开始私聊哦").show();
                    return;
                }
                LogUtil.d("与" + userId + "开始聊天");
                try {
                    // 点击联系人，直接跳转进入聊天界面
                    Intent intent = new Intent(PersonalActivity.this, LCIMConversationActivity.class);
                    // 传入对方的 Id 即可
                    intent.putExtra(LCIMConstants.PEER_ID, String.valueOf(userId));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d("LeanCloud开启聊天异常");
                }

            });
        }

        //设置actionBar位置
        myActionBar = findViewById(R.id.my_action_bar);
        myActionBar.setOnBackClickListener(v -> finish());
        //        myActionBar.set
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.topMargin = ImmersionBar.getStatusBarHeight(this);
        myActionBar.setLayoutParams(layoutParams);
    }

    private void addEvents() {
        //ScrollView滑动监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                //                第二个参数:scrollX是目前的（滑动后）的X轴坐标
                //                第三个参数:ScrollY是目前的（滑动后）的Y轴坐标
                //                第四个参数:oldScrollX是之前的（滑动前）的X轴坐标
                //                第五个参数:oldScrollY是之前的（滑动前）的Y轴坐标
                //                LogUtil.d("scrollY=" + scrollY);
                //                ImmersionBar.with(this).statusBarColor(R.color.red);
                int[] location = new int[2];
                iv_user_bg.getLocationInWindow(location);
                //                LogUtil.d("bgHeight=" + iv_user_bg.getHeight());
                //                LogUtil.d("statusBatHeight" + ImmersionBar.getStatusBarHeight(this));
                //                LogUtil.d("myActionBarHeight" + myActionBar.getHeight());
                int hideY = iv_user_bg.getHeight() - ImmersionBar.getStatusBarHeight(this) - myActionBar.getHeight();
                //                LogUtil.d("hideY="+hideY);
                float alpha = (float) scrollY / hideY;
                alpha = alpha > 1 ? 1.0f : alpha;
                //                LogUtil.d("alpha=" + alpha);
                if (alpha < 0.5f) {
                    myActionBar.setBackImg(R.drawable.ic_back2_white);
                    myActionBar.setActionBarTitle("");
                } else {
                    myActionBar.setBackImg(R.drawable.ic_back2);
                    myActionBar.setActionBarTitle(tv_username.getText().toString());
                }
                ImmersionBar.with(PersonalActivity.this)
                        .statusBarAlpha(alpha)  //状态栏透明度
                        .statusBarColorTransform(R.color.default_color_primary_dark) //状态栏变色后的颜色
                        .addViewSupportTransformColor(myActionBar, R.color.default_color_primary)
                        .init();
            });
        }
    }

    /**
     * 获取用户详细信息并显示
     */
    private void getDetailUserInfo() {
        TextView tv_no_notes = findViewById(R.id.tv_no_notes);

        TravelingUser.getDetailUserInfo(userId, new DetailUserInfoCallback() {
            @Override
            public void onSuccess(DetailUserInfo detailUserInfo, boolean isFocus) {
                noteList = detailUserInfo.getNotes();
                focusNum.setText(String.valueOf(detailUserInfo.getFocusNum()));
                fansNum.setText(String.valueOf(detailUserInfo.getFansNum()));
                collectionsNum.setText(String.valueOf(detailUserInfo.getBeLikeNum()));
                tv_username.setText(detailUserInfo.getNickName());
                PersonalActivity.this.isFocus = isFocus;
                setFocus(isFocus);
                Picasso.get().load(detailUserInfo.getImg()).error(R.drawable.err_img_bg).fit().into(user_img);
                Picasso.get().load(detailUserInfo.getBackgroundImg()).error(R.drawable.err_img_bg).fit().into(iv_user_bg);
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    Note note = detailUserInfo.getNotes().get(position);
                    Intent intent = new Intent(PersonalActivity.this, NewsActivity.class);
                    intent.putExtra(NewsActivity.POSITION, position);
                    intent.putExtra(NewsActivity.s_NOTE, note);
                    startActivityForResult(intent, 1);  //注意 跳转过去后setResult后需Finish()才会正确传回结果来！
                });
                if (detailUserInfo.getNotes().size() == 0) {
                    tv_no_notes.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    return;
                }
                tv_no_notes.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                newsAdapter = new NewsAdapter(PersonalActivity.this, detailUserInfo.getNotes());
                listView.setAdapter(newsAdapter);
                scrollView.scrollTo(0, 0);  //滚动到顶部
            }

            @Override
            public void onFailure(String reason) {
                UtilTools.toast(PersonalActivity.this, "请求失败：" + reason);
            }
        });
    }

    //设置 关注按钮 样式 isFocus为点击的关注状态
    private void setFocus(boolean isFocus) {
        if (isFocus) {
            tv_focus.setText("已关注");
            tv_focus.setTextColor(getResources().getColor(R.color.dialog_orange));
        } else {
            tv_focus.setText("+关注");
            tv_focus.setTextColor(0x8A000000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == NewsActivity.RESULT_CODE) {
            //统一点赞情况
            Note note = (Note) data.getSerializableExtra(NewsActivity.s_NOTE);
            int position = data.getIntExtra(NewsActivity.POSITION, -1);
            if (position >= 0 && position < noteList.size()) {
                noteList.set(position, note);
            }
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
