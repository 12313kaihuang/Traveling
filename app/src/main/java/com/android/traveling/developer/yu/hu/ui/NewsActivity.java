package com.android.traveling.developer.yu.hu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.note.Note;
import com.android.traveling.ui.BackableActivity;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.ui
 * 文件名：NewsActivity
 * 创建者：HY
 * 创建时间：2018/9/27 15:43
 * 描述：  游记/攻略详情页面
 */

public class NewsActivity extends BackableActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initView();

        Intent intent = getIntent();
        Note note = (Note) intent.getSerializableExtra("note");
        setTitle(note.getTitle());  //设置标题

        textView.setText(note.getTitle());

    }

    private void initView() {
        textView = findViewById(R.id.textview);
    }
}
