package com.yu.hu.common.utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * create by hy on 2019/11/27 21:39
 * <p>
 * 线程池
 * 使用RxJava实现 线程的切换
 *
 * @see #io(Runnable) io
 * @see #main(Runnable) 主线程
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ThreadPool {

    public static void io(Runnable runnable) {
        Schedulers.single().scheduleDirect(runnable);
    }

    public static void main(Runnable runnable) {
        AndroidSchedulers.mainThread().scheduleDirect(runnable);
    }
}
