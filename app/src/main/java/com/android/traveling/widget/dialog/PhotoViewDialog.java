package com.android.traveling.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget.dialog
 * 文件名：ChoosePhotoDialog
 * 创建者：HY
 * 创建时间：2019/3/5 19:14
 * 描述：  用于切换头像时选择照片或拍照
 */

public class PhotoViewDialog extends Dialog {

    private TextView tv_back;
    private ImageView iv_rubbish;
    private Bitmap imgBitmap;
    private String imgUrl;
    private OnBtnClickListener clickListener;


    /**
     * 构造函数
     *
     * @param context context
     * @param imgUrl  imgUrl
     */
    public PhotoViewDialog(@NonNull Context context, String imgUrl) {
        this(context);
        this.imgUrl = imgUrl;
    }

    /**
     * 构造函数
     *
     * @param context       context
     * @param imgUri        imgUri
     * @param clickListener clickListener
     */
    public PhotoViewDialog(@NonNull Context context, Uri imgUri, OnBtnClickListener clickListener) {
        this(context);
        this.clickListener = clickListener;
        try {
            imgBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //定义模板
    private PhotoViewDialog(@NonNull Context context) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, R.layout.dialog_photo_view,
                R.style.Theme_photoView_dialog, Gravity.CENTER, R.style.pop_anim_style);
    }

    //定义属性
    private PhotoViewDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
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
        tv_back = findViewById(R.id.tv_back);
        iv_rubbish = findViewById(R.id.iv_rubbish);
        PhotoView mPhotoView = findViewById(R.id.mPhotoView);

        if (imgBitmap != null) {
            mPhotoView.setImageBitmap(imgBitmap);
        }else {
            Picasso.get().load(imgUrl).into(mPhotoView);
        }

        if (clickListener == null) {
            iv_rubbish.setVisibility(View.GONE);
        }

        PhotoViewAttacher attacher = new PhotoViewAttacher(mPhotoView);
        attacher.update();
    }

    /**
     * 添加事件
     */
    private void addEvents() {
        tv_back.setOnClickListener(v -> dismiss());
        iv_rubbish.setOnClickListener(v -> {
            clickListener.onRubbishImgClick(v);
            dismiss();
        });
    }


    public interface OnBtnClickListener {
        void onRubbishImgClick(View v);
    }
}
