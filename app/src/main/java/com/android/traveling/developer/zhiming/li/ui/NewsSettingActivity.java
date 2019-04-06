package com.android.traveling.developer.zhiming.li.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.traveling.R;
import com.android.traveling.ui.BackableActivity;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：NewsSettingActivity
 * 创建者：HY
 * 创建时间：2018/10/19 17:58
 * 描述：  消息设置
 */

public class NewsSettingActivity extends BackableActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_setting);
    }
}
