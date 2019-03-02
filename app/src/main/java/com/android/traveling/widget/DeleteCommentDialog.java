package com.android.traveling.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget
 * 文件名：ReplyDialog
 * 创建者：HY
 * 创建时间：2019/2/27 21:39
 * 描述：  评论/回复 Dialog
 */

@SuppressWarnings("unused")
public class DeleteCommentDialog extends Dialog {

    private DeleteBtnClickListener deleteBtnClickListener;
    private Button btn_confirm_delete;
    private Button btn_cancel;

    public DeleteCommentDialog(@NonNull Context context, DeleteBtnClickListener deleteBtnClickListener) {
        this(context);
        this.deleteBtnClickListener = deleteBtnClickListener;
    }

    //定义模板
    private DeleteCommentDialog(@NonNull Context context) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_delete_comment,
                R.style.Theme_loading_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
    }

    //定义属性
    private DeleteCommentDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
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
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_confirm_delete = findViewById(R.id.btn_confirm_delete);
    }

    //添加事件
    private void addEvents() {

        btn_cancel.setOnClickListener(v -> dismiss());

        btn_confirm_delete.setOnClickListener(v -> {
            deleteBtnClickListener.onClick(v);
            dismiss();
        });

    }

    public interface DeleteBtnClickListener {
        void onClick(View v);
    }
}
