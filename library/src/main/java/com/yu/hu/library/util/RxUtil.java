package com.yu.hu.library.util;

import android.os.Bundle;

import com.yu.hu.library.activity.BaseSplashActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.library.util
 * 文件名：RxUtil
 * 创建者：HY
 * 创建时间：2019/6/20 18:10
 * 描述：  RxJava相关工具包
 */
@SuppressWarnings("unused")
public class RxUtil {


    /**
     * 延时一段时间
     *
     * @param delay delay
     * @param unit  unit {@link TimeUnit#SECONDS ...}
     */
    public static Observable<Long> timer(long delay, TimeUnit unit) {
        return Observable.timer(delay, unit);
    }

    /**
     * subscribeOn Schedulers.io()
     * observeOn mainThread()
     * <p>
     * 使用{@code compose}方法进行Scheduler转换
     *
     * @see BaseSplashActivity#onPrepare(Bundle)
     */
    public static <T> Observable.Transformer<T, T> defaultSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
