package com.android.traveling.application;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.android.traveling.util.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.application
 * 文件名：BaseApplication
 * 创建者：HY
 * 创建时间：2018/9/22 11:49
 * 描述：  BaseApplication
 * <p>
 * 初始化一些服务
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //litepal
        LitePal.initialize(this);

        //讯飞SDK
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5c518c16");

        //相机权限
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
