package com.yu.hu.emoji.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Hy
 * created on 2020/04/22 17:31
 * <p>
 * 软键盘工具类
 **/
@SuppressWarnings("unused")
public class KeyBoardUtils {

    private static Application sApplication;


    /**
     * 隐藏软键盘
     *
     * @param view The view.
     */
    public static void hideSoftInput(@NonNull final View view) {
        InputMethodManager imm =
                (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Show the soft input.
     *
     * @param view The view.
     */
    public static void showSoftInput(@NonNull final View view) {
        showSoftInput(view, 0);
    }

    /**
     * Show the soft input.
     *
     * @param view  The view.
     * @param flags Provides additional operating flags.  Currently may be
     *              0 or have the {@link InputMethodManager#SHOW_IMPLICIT} bit set.
     */
    @SuppressWarnings("WeakerAccess")
    public static void showSoftInput(@NonNull final View view, final int flags) {
        InputMethodManager imm =
                (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, flags, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                        || resultCode == InputMethodManager.RESULT_HIDDEN) {
                    toggleSoftInput();
                }
            }
        });
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * Toggle the soft input display or not.
     */
    @SuppressWarnings("WeakerAccess")
    public static void toggleSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(0, 0);
    }


    private static Application getApplication(){
        if (sApplication == null) {
            sApplication = getApplicationByReflect();
        }
        return sApplication;
    }

    /**
     * 通过反射获取Application
     *
     * @return Application
     */
    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }
}
