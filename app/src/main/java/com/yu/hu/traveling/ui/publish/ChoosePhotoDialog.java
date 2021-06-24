package com.yu.hu.traveling.ui.publish;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.DialogChoosePhotoBinding;

/**
 * @author Hy
 * created on 2020/04/24 22:10
 **/
public class ChoosePhotoDialog extends DialogFragment implements View.OnClickListener {

    private Builder mBuilder;
    private DialogChoosePhotoBinding mDataBinding;

    public ChoosePhotoDialog() {
        this(new Builder());
    }

    private ChoosePhotoDialog(Builder mBuilder) {
        this.mBuilder = mBuilder;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DialogChoosePhotoBinding.inflate(inflater, container, false);
        setStyle();
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDataBinding.takePhoto.setOnClickListener(this);
        mDataBinding.albumBtn.setOnClickListener(this);
        mDataBinding.cancelBtn.setOnClickListener(this);
        mDataBinding.chooseVideo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (mBuilder.onItemClickListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.take_photo:
                mBuilder.onItemClickListener.onTakePhotoBtnClicked(this);
                break;
            case R.id.album_btn:
                mBuilder.onItemClickListener.onChoosePhotoBtnClicked(this);
                break;
            case R.id.choose_video:
                mBuilder.onItemClickListener.onChooseVideoBtnClicked(this);
                break;
            case R.id.cancel_btn:
                mBuilder.onItemClickListener.onCancelBtnClicked(this);
                dismiss();
        }
    }

    /**
     * 有关style的一些初始化操作
     * 如果设置背景透明等
     */
    private void setStyle() {
        //无title  自定义主题
        setStyle(DialogFragment.STYLE_NO_TITLE, mBuilder.theme);

        Dialog dialog = getDialog();
        if (dialog == null) {
            Log.w(getClass().getSimpleName(), "dialog == null");
            return;
        }

        //dialog无title
        dialog.requestWindowFeature(STYLE_NO_TITLE);

        Window window = dialog.getWindow();
        if (window == null) {
            Log.w(getClass().getSimpleName(), "dialog.window == null");
            return;
        }

        //透明背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.yu.hu.emoji.R.color.transparent, null)));
        } else {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.yu.hu.emoji.R.color.transparent)));
        }

        //设置宽高
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = mBuilder.width;
        attributes.height = mBuilder.height;
        attributes.gravity = mBuilder.gravity;
        //设置动画
        window.setWindowAnimations(mBuilder.animation);
        window.setAttributes(attributes);
    }

    public static class Builder {

        int width = WindowManager.LayoutParams.MATCH_PARENT;

        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        int gravity = Gravity.BOTTOM;

        @StyleRes
        int theme = com.yu.hu.emoji.R.style.BaseEmojiDialog;

        @StyleRes
        int animation = com.yu.hu.emoji.R.style.PopStyle;

        OnItemClickListener onItemClickListener;

        public Builder setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public ChoosePhotoDialog build() {
            return new ChoosePhotoDialog(this);
        }

        public void show(FragmentManager fragmentManager) {
            build().show(fragmentManager, null);
        }
    }

    public interface OnItemClickListener {
        void onTakePhotoBtnClicked(ChoosePhotoDialog dialog);

        void onChoosePhotoBtnClicked(ChoosePhotoDialog dialog);

        void onChooseVideoBtnClicked(ChoosePhotoDialog dialog);

        void onCancelBtnClicked(ChoosePhotoDialog dialog);
    }
}
