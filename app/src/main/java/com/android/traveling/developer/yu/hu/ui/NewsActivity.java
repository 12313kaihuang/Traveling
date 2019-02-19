package com.android.traveling.developer.yu.hu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.note.Note;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.DateUtil;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

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

    CircleImageView user_bg;
    TextView tv_username;
    TextView tv_time;
    TextView tv_level;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initView();

        Intent intent = getIntent();
        Note note = (Note) intent.getSerializableExtra(s_NOTE);
        setTitle(note.getTitle());  //设置标题

        //头像
        Picasso.get().load(note.getReleasePeople().getImgUrl()).into(user_bg);
        tv_username.setText(note.getReleasePeople().getNickName());
        tv_time.setText(DateUtil.fromNow(note.getCreateTime()));
        tv_level.setText(String.format(getString(R.string.level), "LV."
                , note.getReleasePeople().getLevel()));
    }

    private void initView() {
        user_bg = findViewById(R.id.user_bg);
        tv_username = findViewById(R.id.tv_username);
        tv_time = findViewById(R.id.tv_time);
        tv_level = findViewById(R.id.tv_level);
    }
}
