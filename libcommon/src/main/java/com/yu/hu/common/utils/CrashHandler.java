package com.yu.hu.common.utils;

import android.util.Log;


/**
 * create by hy on 2019/11/26 23:31
 * <p>
 * 全局异常捕获
 * 部分代码还是用了AndroidUtilCode中的
 * <p>
 * 需要初始化{@link #init()}或{@link #init(OnCrashListener)}才会生效
 *
 * @see Log#getStackTraceString(Throwable)
 * @see com.blankj.utilcode.util.CrashUtils
 * @see com.blankj.utilcode.util.ThrowableUtils
 */
@SuppressWarnings({"unused", "NullableProblems"})
public class CrashHandler {

    private static final String TAG = "CrashHandler";

    private static final Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;
    private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER;

    private static OnCrashListener sOnCrashListener;

    public static void init() {
        init(null);
    }

    /**
     * 可以在其中执行重启操作等
     *
     * @param listener listener
     */
    @SuppressWarnings("WeakerAccess")
    public static void init(OnCrashListener listener) {
        Thread.setDefaultUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
        sOnCrashListener = listener;
    }

    static {
        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

        UNCAUGHT_EXCEPTION_HANDLER = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                Log.e(TAG, "uncaughtException: ", e);
                if (sOnCrashListener != null) {
                    sOnCrashListener.onCrash(e);
                }
            }
        };
    }

    public interface OnCrashListener {
        void onCrash(Throwable e);
    }
}
