package com.yu.hu.common.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.SoftReference;

/**
 * Created by Hy on 2019/11/28 19:39
 *
 * ActivityLifecycleCallbacksImpl
 * 用于获取当前站顶activity
 **/
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    private SoftReference<Activity> mTopActivity;

    @SuppressWarnings("WeakerAccess")
    @Nullable
    public Activity getTopActivity() {
        return mTopActivity.get();
    }

    private void setTopActivity(Activity Activity) {
        this.mTopActivity = new SoftReference<>(Activity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        setTopActivity(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        setTopActivity(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        setTopActivity(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
