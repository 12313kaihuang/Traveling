package com.yu.hu.traveling.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.yu.hu.library.widget.dialog.DialogBuilder;
import com.yu.hu.traveling.R;

import androidx.annotation.NonNull;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.widget
 * 文件名：ReplyDialog
 * 创建者：HY
 * 创建时间：2019/7/3 16:04
 * 描述：  发表评论 dialog
 */
public class ReplyDialog extends Dialog {

    private ReplyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder extends DialogBuilder<Builder, ReplyDialog> {

        private String hint;

        private OnPublishListener mPublishListener;

        private Button mPublishBtn;

        public Builder(Context context) {
            super(context, R.layout.dialog_reply);
            mThemeResId = R.style.Theme_comment_dialog;
            mDialogGravity = Gravity.BOTTOM;
            mDialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
        }

        @Override
        public void onCreated(Dialog dialog) {
            EditText content = findViewById(R.id.et_comment);
            mPublishBtn = findViewById(R.id.btn_publish);

            content.setHint(hint);
            content.addTextChangedListener(mTextWatcher);

            mPublishBtn.setOnClickListener(v -> {
                if (TextUtils.isEmpty(content.getText())) {
                    return;
                }
                if (mPublishListener != null) {
                    mPublishListener.onPublish(v, content.getText().toString());
                }
                mDialog.dismiss();
            });
        }

        @Override
        protected ReplyDialog onCreateDialog() {
            return new ReplyDialog(mContext, mThemeResId);
        }

        @Override
        protected Builder returnThis() {
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        /**
         * 发表按钮 监听事件
         */
        public Builder setOnPublishListener(OnPublishListener publishListener) {
            this.mPublishListener = publishListener;
            return this;
        }

        public interface OnPublishListener {
            void onPublish(View v, String content);
        }

        /**
         * 输入监听
         */
        private TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mPublishBtn.setBackgroundColor(mContext.getResources().getColor(R.color.publish_bg_color));
                    mPublishBtn.setTextColor(mContext.getResources().getColor(R.color.publish_text_color));
                } else {
                    mPublishBtn.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
                    mPublishBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
