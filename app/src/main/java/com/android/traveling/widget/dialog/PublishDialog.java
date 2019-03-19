package com.android.traveling.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget.dialog
 * 文件名：ChoosePhotoDialog
 * 创建者：HY
 * 创建时间：2019/3/5 19:14
 * 描述：  发布游记/攻略用
 */

public class PublishDialog extends Dialog {


    //定义模板
    public PublishDialog(@NonNull Context context) {
        this(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_loading,
                R.style.Theme_loading_dialog, Gravity.CENTER, R.style.pop_anim_style);
    }

    //定义属性
    private PublishDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        TextView hint = findViewById(R.id.tv_hint);
        hint.setText("发表中...");
        addEvents();

    }

    /**
     * 添加事件
     */
    private void addEvents() {

    }


}
