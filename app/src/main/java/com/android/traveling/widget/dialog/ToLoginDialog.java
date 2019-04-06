package com.android.traveling.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.zhiming.li.ui.LoginActivity;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget
 * 文件名：ReplyDialog
 * 创建者：HY
 * 创建时间：2019/2/27 21:39
 * 描述：  选择是否去登陆的Dialog
 */

@SuppressWarnings("unused")
public class ToLoginDialog extends Dialog {

    private TextView tv_cancel;
    TextView tv_to_login;

    private String hint;

    public ToLoginDialog(@NonNull Context context, String hint) {
        this(context);
        this.hint = hint;
    }

    //定义模板
    private ToLoginDialog(@NonNull Context context) {
        this(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_to_login,
                R.style.Theme_loading_dialog, Gravity.CENTER, R.style.pop_anim_style);
    }

    //定义属性
    private ToLoginDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
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
        addEvents();
    }

    //初始化
    private void initView() {
        TextView tv_hint = findViewById(R.id.tv_hint);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_to_login = findViewById(R.id.tv_to_login);

        if (hint != null && hint.length() != 0) {
            tv_hint.setText(hint);
        }
    }

    //添加事件
    private void addEvents() {

        tv_cancel.setOnClickListener(v -> dismiss());

        tv_to_login.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getContext(), LoginActivity.class));
            dismiss();
        });

    }

    public interface DeleteBtnClickListener {
        void onClick(View v);
    }
}
