package com.yu.hu.ninegridlayout.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.DisplayMetrics;

import java.lang.reflect.InvocationTargetException;

public class PixUtils {

    private static volatile Application sApplication;

    public static int dp2px(int dpValue) {
        DisplayMetrics metrics = getApplication().getResources().getDisplayMetrics();
        return (int) (metrics.density * dpValue + 0.5f);
    }

    public static int getScreenWidth() {
        DisplayMetrics metrics = getApplication().getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    @SuppressWarnings("unused")
    public static int getScreenHeight() {
        DisplayMetrics metrics = getApplication().getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    @SuppressLint("PrivateApi")
    @SuppressWarnings("WeakerAccess")
    public static Application getApplication() {
        if (sApplication == null) {
            try {
                sApplication = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication")
                        .invoke(null, (Object[]) null);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sApplication;
    }
}
