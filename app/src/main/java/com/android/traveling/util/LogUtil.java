package com.android.traveling.util;

import android.util.Log;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.util
 * 文件名：LogUtil
 * 创建者：HY
 * 创建时间：2018/9/22 10:48
 * 描述：  Log工具类
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class LogUtil {

    //开关
    public static boolean DEBUG = true;
    //TAG
    private static String TAG = "Traveling";

    //四个等级 DIWE
    public static void d(String text) {
        d(TAG, text);
    }

    public static void i(String text) {
        i(TAG, text);
    }

    public static void w(String text) {
        w(TAG, text);
    }

    public static void e(String text) {
        e(TAG, text);
    }

    public static void d(String TAG, String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String TAG, String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String TAG, String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String TAG, String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

}
