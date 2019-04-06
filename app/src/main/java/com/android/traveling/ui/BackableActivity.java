package com.android.traveling.ui;

import com.android.traveling.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.android.traveling.util.LogUtil;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.ui
 * 文件名：BackableActivity
 * 创建者：HY
 * 创建时间：2018/9/22 11:57
 * 描述：  Activity基类
 * <p>
 * * 主要做的事情：
 * 1.统一的属性
 * 2.统一的接口
 */


@SuppressLint("Registered")
public class BackableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置主题
        setTheme(R.style.BackableActivity);

        //显示返回键
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            LogUtil.e("BackableActivity初始化失败");
        }
    }


    //菜单栏操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回键的id
            case android.R.id.home:
                onBack();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onBack();
        finish();
    }

    //按下返回键触发(上部返回键或底部返回键)
    public void onBack(){

    }
}
