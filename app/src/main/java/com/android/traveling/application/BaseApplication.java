package com.android.traveling.application;

import android.app.Application;

import com.android.traveling.util.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.application
 * 文件名：BaseApplication
 * 创建者：HY
 * 创建时间：2018/9/22 11:49
 * 描述：  BaseApplication
 *
 * 初始化一些服务
 *
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //litepal
        LitePal.initialize(this);
    }
}
