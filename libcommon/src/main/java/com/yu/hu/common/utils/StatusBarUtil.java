package com.yu.hu.common.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;

/**
 * @author Hy
 * created on 2020/04/14 20:49
 * <p>
 * StatusBar工具类
 **/
@SuppressWarnings("unused")
public class StatusBarUtil {

    /**
     * 6.0及以上的沉浸式布局
     *
     * @param activity activity
     */
    public static void fitSystemBar(Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN--能够使得我们的页面布局延伸到状态栏之下，但不会隐藏状态栏。也就相当于状态栏是遮盖在布局之上的
        //View.SYSTEM_UI_FLAG_FULLSCREEN -- 能够使得我们的页面布局延伸到状态栏，但是会隐藏状态栏。
        //WindowManager.LayoutParams.FLAG_FULLSCREEN
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置状态栏的色调
     * Android 6.0（23）及以上有用
     * 同  {@link BarUtils#setStatusBarLightMode(Activity, boolean)}
     *
     * @param activity activity
     * @param isLight  true 为浅色模式（白底黑字），反之黑底白字
     */
    public static void setStatusBarMode(Activity activity, boolean isLight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int visibility = decorView.getSystemUiVisibility();
            if (isLight) {
                visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                visibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(visibility);
        }
    }

}
