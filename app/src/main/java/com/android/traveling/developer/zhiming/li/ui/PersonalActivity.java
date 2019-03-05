package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.util.UtilTools;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：PersonalActivity
 * 创建者：HY
 * 创建时间：2019/2/26 7:49
 * 描述：  个人主页
 */

public class PersonalActivity extends AppCompatActivity{

    public static final String USER_ID = "userID";

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();
        Intent intent = getIntent();
        int userID = intent.getIntExtra(USER_ID, 0);
        if (userID == 0) {
            UtilTools.toast(this,"userID传参有误");
            finish();
        }
        textView.setText(String.format("userId=%d", userID));
    }

    private void initView() {
        textView = findViewById(R.id.tv_userid);
    }
}
