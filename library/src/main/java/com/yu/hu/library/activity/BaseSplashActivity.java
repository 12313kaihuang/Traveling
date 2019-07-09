package com.yu.hu.library.activity;

import android.os.Bundle;

import com.yu.hu.library.util.RxUtil;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import rx.Subscription;

/**
 * 文件名：BaseSplashActivity
 * 创建者：HY
 * 创建时间：2019/6/21 15:45
 * 描述：  闪屏页基类  隔一段时间后跳转（一般是MainActivity）
 *
 * @see #getSplashDelay() 有默认值
 * @see #getSplashTimeUnit() 有默认值
 * @see #skipTo() 执行跳转相关逻辑
 */
@SuppressWarnings("unused")
public abstract class BaseSplashActivity extends BaseActivity {

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {

        //指定时间后跳转
        Subscription subscription = RxUtil.timer(getSplashDelay(), getSplashTimeUnit())
                .compose(RxUtil.defaultSchedulers())
                .subscribe(aLong -> {
                    skipTo();
                    finish();
                });
        addSubscription(subscription);
    }

    /**
     * 执行跳转相关操作
     */
    protected abstract void skipTo();

    /**
     * 闪屏时间
     */
    protected long getSplashDelay() {
        return 2;
    }

    /**
     * 闪屏时间{@link #getSplashDelay()}的单位
     */
    protected TimeUnit getSplashTimeUnit() {
        return TimeUnit.SECONDS;
    }

    //禁用返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
