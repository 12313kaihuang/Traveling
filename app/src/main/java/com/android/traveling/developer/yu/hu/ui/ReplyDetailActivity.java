package com.android.traveling.developer.yu.hu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.ui.BackableActivity;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.ui
 * 文件名：ReplyDetailActivity
 * 创建者：HY
 * 创建时间：2019/2/25 23:30
 * 描述：  评论详情
 */

public class ReplyDetailActivity extends BackableActivity {

    public static final String USER_ID = "userID";

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_detail);
        initView();
        setTitle("回复详情");
        Intent intent = getIntent();
        String userId = intent.getStringExtra(USER_ID);
        textView.setText(String.format("userid=%s", userId));
    }

    private void initView() {

        textView = findViewById(R.id.text);
    }
}
