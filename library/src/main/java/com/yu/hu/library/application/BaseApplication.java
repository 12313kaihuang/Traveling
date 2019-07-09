package com.yu.hu.library.application;

import android.app.Application;

import com.yu.hu.library.util.LogUtil;
import com.yu.hu.library.util.SharedPreferencesUtil;

/**
 * 文件名：BaseApplication
 * 创建者：HY
 * 创建时间：2019/6/10 9:22
 * 描述：  Application基类 项目Application需继承此类
 * <p>
 * 建议重写{@link #getApplicationName()}方法
 */
@SuppressWarnings("unused")
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //LogUtil
        LogUtil.TAG = getApplicationName();

        //SharedPreferencesUtil
        SharedPreferencesUtil.init(getApplicationName(), this);

    }

    /**
     * App的名字
     * <p>
     * 如果继承此类重写此方法并返回App名字
     * 主要在工具包中需要用到  例如：
     * {@link com.yu.hu.library.util.LogUtil#TAG}
     */
    protected String getApplicationName() {
        return "application_name";
    }
}
