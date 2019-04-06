package com.android.traveling.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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
public class ReplyDialog extends Dialog {

    private PublishBtnClickListener publishBtnClickListener;
    private EditText et_comment;
    private Button btn_publish;

    private Context context;
    private String hint = null;

    public ReplyDialog(@NonNull Context context, PublishBtnClickListener publishBtnClickListener) {
        this(context);
        this.publishBtnClickListener = publishBtnClickListener;
    }

    public ReplyDialog(@NonNull Context context, String hint, PublishBtnClickListener publishBtnClickListener) {
        this(context);
        this.hint = hint;
        this.publishBtnClickListener = publishBtnClickListener;
    }

    //定义模板
    private ReplyDialog(@NonNull Context context) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_reply,
                R.style.Theme_comment_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
    }

    //定义属性
    private ReplyDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
        super(context, style);
        //设置属性
        this.context = context;
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
        btn_publish = findViewById(R.id.btn_publish);
        et_comment = findViewById(R.id.et_comment);

        if (hint != null) {
            et_comment.setHint(hint);
        }
    }

    //添加事件
    private void addEvents() {
        //输入监听
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btn_publish.setBackgroundColor(context.getResources().getColor(R.color.publish_bg_color));
                    btn_publish.setTextColor(context.getResources().getColor(R.color.publish_text_color));
                }else {
                    btn_publish.setBackgroundColor(context.getResources().getColor(R.color.blue));
                    btn_publish.setTextColor(context.getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_publish.setOnClickListener(v -> {
            if (TextUtils.isEmpty(et_comment.getText())) {
                return;
            }
            publishBtnClickListener.onClick(v, et_comment.getText().toString());
            dismiss();
        });
    }

    public interface PublishBtnClickListener {
        void onClick(View v,String content);
    }
}
