package com.yu.hu.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.yu.hu.library.R;
import com.yu.hu.library.util.LogUtil;

import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;


/**
 * 文件名：HLoadingDialog
 * 创建者：HY
 * 创建时间：2019/6/26 16:58
 * 描述：  正在加载的dialog
 */
@SuppressWarnings("unused")
public class HLoadingDialog extends Dialog {

    private HLoadingDialog(@NonNull Context context) {
        super(context);
    }

    private HLoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * 创建一个默认样式的 HLoadingDialog（加载中）
     *
     * @param context context
     */
    public static HLoadingDialog getDefault(Context context) {
        return new HLoadingDialog.Builder(context).build();
    }

    /**
     * 创建一个默认样式的 HLoadingDialog（加载中）
     *
     * @param context context
     */
    public static HLoadingDialog getDefault(Context context,String hint) {
        return new HLoadingDialog.Builder(context)
                .setHint(hint)
                .build();
    }


    /**
     * 用于构建Dialog
     */
    @SuppressWarnings("WeakerAccess")
    public static class Builder extends DialogBuilder<Builder, HLoadingDialog> {

        //提示文字
        private String mHint;

        //progressBar样式
        @DrawableRes
        private int mProgressBarStyleRes;

        public Builder(Context context) {
            super(context, R.layout.widget_loading_dialog);
        }

        @Override
        protected HLoadingDialog onCreateDialog() {
            return new HLoadingDialog(mContext, mThemeResId);
        }

        @Override
        protected void onInitParams() {
            mHint = mDialog.getContext().getString(R.string.on_loading);
            mProgressBarStyleRes = Integer.MAX_VALUE;
        }

        @Override
        public Builder returnThis() {
            return this;
        }

        @CallSuper
        @Override
        public void onCreated(Dialog dialog) {
            TextView hint = findViewById(R.id.tv_hint);
            hint.setText(mHint);

            if (mProgressBarStyleRes != Integer.MAX_VALUE) {
                LogUtil.d("自定义progressBar样式");
                ProgressBar progressBar = findViewById(R.id.progress_bar);
                progressBar.setIndeterminateDrawable(mDialog.getContext().getDrawable(mProgressBarStyleRes));
            }
        }

        /**
         * 设置加载文字
         */
        public Builder setHint(String hint) {
            this.mHint = hint;
            return this;
        }

        /**
         * 设置ProgressBar的样式
         * 可以参考{@code R.drawable.progress_bar_default.xml}
         */
        public Builder setIndeterminateDrawable(@DrawableRes int drawableRes) {
            this.mProgressBarStyleRes = drawableRes;
            return this;
        }
    }
}
