package com.yu.hu.common.application;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.Nullable;

/**
 * Created by Hy on 2019/11/28 19:38
 **/
@SuppressWarnings("unused")
public class BaseApplication extends Application {

    private ActivityLifecycleCallbacksImpl mActivityLifecycleImpl;

    @Nullable
    public Activity getTopActivity() {
        return mActivityLifecycleImpl.getTopActivity();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityLifecycleImpl = new ActivityLifecycleCallbacksImpl();
        registerActivityLifecycleCallbacks(mActivityLifecycleImpl);
    }
}
