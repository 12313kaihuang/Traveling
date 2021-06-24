package com.yu.hu.common.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * create by hy on 2019/11/18 23:22
 * <p>
 * logUtil
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class LogUtil {

    /**
     * 是否开启debug（是否打印相应log）
     *
     * @see Common#DEBUG_LOG
     */
    private static final boolean debug = Common.DEBUG_LOG;

    private static final String TAG = LogUtil.class.getSimpleName();

    public static void d(String content) {
        d(TAG, content);
    }

    public static void i(String content) {
        i(TAG, content);
    }

    public static void w(String content) {
        w(TAG, content);
    }

    public static void e(String content) {
        e(TAG, content);
    }

    /**
     * 不受{@link #debug}控制
     * 用于一些允许范围为的异常捕获的打印
     */
    public static void error(String content) {
        e(TAG, content);
    }

    /**
     * 不受{@link #debug}控制
     * 用于一些重要信息的打印
     */
    public static void warn(String content) {
        warn(TAG, content);
    }


    public static void d(final String tag, String content) {
        if (debug) {
            Log.d(tag, content);
        }
    }

    public static void i(final String tag, String content) {
        if (debug) {
            Log.i(TextUtils.equals(TAG, tag) ? TAG : TAG + "-" + tag, content);
        }
    }

    public static void w(final String tag, String content) {
        if (debug) {
            Log.w(tag, content);
        }
    }

    public static void e(final String tag, String content) {
        if (debug) {
            Log.e(tag, content);
        }
    }

    public static void info(final String tag, String content) {
        Log.i(tag, content);
    }

    public static void warn(final String tag, String content) {
        Log.w(tag, content);
    }

}
