package com.yu.hu.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;


/**
 * 文件名：DialogFactory
 * 创建者：HY
 * 创建时间：2019/6/26 18:25
 * 描述：  DialogFactory  通用的Dialog生成工具
 *
 * @see Builder#build()  该方法最后调用，用于生成dialog实例对象
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DialogFactory {

    public static class CustomDialog extends Dialog {

        /**
         * @param context    context
         * @param themeResId 主题id  注意了这里是主题id！
         */
        CustomDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

    }

    /**
     * 用于构建自定义Dialog
     */
    public static class Builder extends DialogBuilder<Builder, CustomDialog> {

        public Builder(Context context, int layoutRes) {
            super(context, layoutRes);
        }

        @Override
        protected CustomDialog onCreateDialog() {
            return new CustomDialog(mContext, mThemeResId);
        }

        @Override
        protected Builder returnThis() {
            return this;
        }
    }
}
