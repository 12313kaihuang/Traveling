package com.android.traveling.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.dialog
 * 文件名：MyCustomDialog
 * 创建者：HY
 * 创建时间：2018/10/17 18:12
 * 描述：  自定义dialog
 */

@SuppressWarnings("unused")
public class MyCustomDialog extends Dialog {

    //定义模板
    public MyCustomDialog(@NonNull Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, layout, style, Gravity.CENTER);
    }

    //定义属性
    private MyCustomDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
        super(context, style);
        //设置属性
        setContentView(layout);
        Window window = getWindow();
        @SuppressWarnings("ConstantConditions")
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(anim);
    }

    //实例
    private MyCustomDialog(Context context, int width, int height, int layout, int style, int gravity) {
        this(context, width, height, layout, style, gravity, R.style.pop_anim_style);
    }

    //加载中,请稍后
    public static MyCustomDialog getLoadingDialog(Context context) {
        MyCustomDialog customDialog = new MyCustomDialog(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loading,
                R.style.Theme_loading_dialog, Gravity.CENTER, R.style.pop_anim_style);
        customDialog.setCancelable(false);
        return customDialog;
    }

    //正在登陆
    public static MyCustomDialog getLoginDialog(Context context) {
        MyCustomDialog customDialog = new MyCustomDialog(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_login,
                R.style.Theme_loading_dialog, Gravity.CENTER, R.style.pop_anim_style);
        customDialog.setCancelable(false);
        return customDialog;
    }


}
