package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
import com.android.traveling.util.UtilTools;
import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    private List<Note> noteList;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ImmersionBar.with(this).init();
        //使得只有顶部沉浸式了而底部没有
        //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Intent intent = getIntent();
        int userID = intent.getIntExtra(USER_ID, 0);
        if (userID == 0) {
            UtilTools.toast(this, "userID传参有误");
            finish();
        }
        initView(userID);
    }

    private void initView(int userID) {
        focusNum = findViewById(R.id.my_focus_num);
        fansNum = findViewById(R.id.my_fans_num);
        collectionsNum = findViewById(R.id.my_collections_num);
        user_img = findViewById(R.id.user_img);
        iv_user_bg = findViewById(R.id.iv_user_bg);
        listView = findViewById(R.id.list_view);
        TextView tv_no_notes = findViewById(R.id.tv_no_notes);
        ImageView back = findViewById(R.id.iv_back);
        scrollView = findViewById(R.id.scrollView);
        back.setOnClickListener(v -> finish());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                //                第二个参数:scrollX是目前的（滑动后）的X轴坐标
                //                第三个参数:ScrollY是目前的（滑动后）的Y轴坐标
                //                第四个参数:oldScrollX是之前的（滑动前）的X轴坐标
                //                第五个参数:oldScrollY是之前的（滑动前）的Y轴坐标

            });
        }

        TravelingUser.getDetailUserInfo(userID, new DetailUserInfoCallback() {
            @Override
            public void onSuccess(DetailUserInfo detailUserInfo) {
                noteList = detailUserInfo.getNotes();
                focusNum.setText(String.valueOf(detailUserInfo.getFocusNum()));
                fansNum.setText(String.valueOf(detailUserInfo.getFansNum()));
                collectionsNum.setText(String.valueOf(detailUserInfo.getBeLikeNum()));
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
