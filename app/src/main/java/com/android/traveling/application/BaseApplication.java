package com.android.traveling.application;

import android.app.Application;
import android.os.StrictMode;

import com.android.traveling.entity.leancloud.CustomUserProvider;
import com.android.traveling.util.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

import cn.leancloud.chatkit.LCChatKit;


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

    private final String APP_ID = "WSYL5OvdOhS1y2m1AMefmgcB-gzGzoHsz";
    private final String APP_KEY = "zgFTp1Vn0gEhGC2lMphmayW1";

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


        //leanClound
//        AVOSCloud.setDebugLogEnabled(true);
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
    }
}
