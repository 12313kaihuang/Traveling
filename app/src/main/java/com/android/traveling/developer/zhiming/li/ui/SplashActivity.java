package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.traveling.MainActivity;
import com.android.traveling.R;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.SharedPreferencesUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：SplashActivity
 * 创建者：HY
 * 创建时间：2018/9/26 19:28
 * 描述：  闪屏页
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initVIew();

        //延时两秒进入主页或者引导页
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (isFirstRunning()) {
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }, 2000);
    }

    //禁用返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    //初始化view
    private void initVIew() {

        TextView tv_splash = findViewById(R.id.tv_splash);
        //设置字体
        UtilTools.setFont(this, tv_splash,StaticClass.HANGOVER_BOLD);
    }


    //判断程序是否第一次运行
    private boolean isFirstRunning() {
        Boolean isFirstRunning =
                SharedPreferencesUtil.getBoolean(this, StaticClass.IS_FIRST_RUN, true);
        if (isFirstRunning) {
            //是第一次运行
            SharedPreferencesUtil.putBoolean(this, StaticClass.IS_FIRST_RUN, false);
        }
        return isFirstRunning;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("SplashActivity onDestroy");
    }
}
