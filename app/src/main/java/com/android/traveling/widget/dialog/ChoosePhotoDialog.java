package com.android.traveling.widget.dialog;

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
 * 包名：  com.android.traveling.widget.dialog
 * 文件名：ChoosePhotoDialog
 * 创建者：HY
 * 创建时间：2019/3/5 19:14
 * 描述：  用于切换头像时选择照片或拍照
 */

public class ChoosePhotoDialog extends Dialog {

    /**
     * 拍照
     */
    private Button btn_take_photo;

    /**
     * 从相册选取
     */
    private Button btn_from_album;

    /**
     * 取消
     */
    private Button btn_cancel;

    /**
     * 点击回调接口
     */
    private OnBtnClickListener onBtnClickListener;

    /**
     * 构造函数
     * @param context context
     * @param listener listener
     */
    public ChoosePhotoDialog(@NonNull Context context, OnBtnClickListener listener) {
        this(context);
        this.onBtnClickListener = listener;
    }

    //定义模板
    private ChoosePhotoDialog(@NonNull Context context) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, R.layout.dialog_choose_photo,
                R.style.Theme_loading_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
    }

    //定义属性
    private ChoosePhotoDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
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

    /**
     * 初始化控件
     */
    private void initView() {
        btn_take_photo = findViewById(R.id.btn_take_photo);
        btn_from_album = findViewById(R.id.btn_from_album);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    /**
     * 添加事件
     */
    private void addEvents() {
        btn_cancel.setOnClickListener(v -> dismiss());
        btn_take_photo.setOnClickListener(v -> onBtnClickListener.onTakePhotoBtnClick(v));
        btn_from_album.setOnClickListener(v -> onBtnClickListener.onFromAlbumBtnClick(v));
    }


    public interface OnBtnClickListener {
        void onTakePhotoBtnClick(View v);

        void onFromAlbumBtnClick(View v);
    }
}
