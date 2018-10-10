package com.android.traveling.application;

import android.app.Application;
import android.content.Context;

import com.android.traveling.R;
import com.avos.avoscloud.AVOSCloud;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

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

        //LeanCloud 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"WSYL5OvdOhS1y2m1AMefmgcB-gzGzoHsz","zgFTp1Vn0gEhGC2lMphmayW1");
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
    }
}
