package com.android.traveling.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.util
 * 文件名：SharedPreferencesUtil
 * 创建者：HY
 * 创建时间：2018/9/22 10:55
 * 描述：  SharedPreference工具类
 */

@SuppressWarnings("unused")
public class SharedPreferencesUtil {

    private static final String NAME = "Traveling";

    //存入String
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    //取出String
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    //存入Int
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    //取出Int
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    //存入Boolean
    @SuppressWarnings("SameParameterValue")
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    //取出Boolean
    @SuppressWarnings("SameParameterValue")
    public static Boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

}
