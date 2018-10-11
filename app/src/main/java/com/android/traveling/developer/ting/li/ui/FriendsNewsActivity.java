package com.android.traveling.developer.ting.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.ting.li.entity.FriendsNews;
import com.android.traveling.ui.BackableActivity;
/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.ting.li.ui
 * 文件名：FriendsNewsActivity
 * 创建者：LT
 * 创建时间：2018/10/4 17:22
 * 描述：  结伴消息详情页面
 */
public class FriendsNewsActivity extends BackableActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsnews_detail);

        /*Intent intent = getIntent();
        FriendsNews friendsnews = (FriendsNews) intent.getSerializableExtra("friendsnews");
        TextView textview = findViewById(R.id.textview);
        if (friendsnews.getUserName() != null) {
            textview.setText(friendsnews.getUserName());
        }else {
            textview.setText(R.string.list_friends_item_username);
        } */
    }
}
