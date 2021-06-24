package com.yu.hu.emoji;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import android.view.inputmethod.EditorInfo;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.yu.hu.emoji.databinding.DialogEmojiCommentBinding;
import com.yu.hu.emoji.entity.EmojiCommentModel;
import com.yu.hu.emoji.utils.KeyBoardUtils;
import com.yu.hu.emoji.utils.TransformUtils;

/**
 * @author Hy
 * created on 2020/04/22 17:10
 * <p>
 * 一个简单的评论发布Dialog，可以输入表情-
 **/
public class EmojiCommentDialog extends DialogFragment {

    private Builder mBuilder;
    private DialogEmojiCommentBinding mDataBinding;

    private EmojiCommentModel mModel;

    public EmojiCommentDialog() {
        this.mBuilder = new Builder();
        mBuilder.hint = getResources().getString(R.string.emoji_comment_hint);
    }

    private EmojiCommentDialog(Builder builder) {
        mBuilder = builder;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DialogEmojiCommentBinding.inflate(inflater, container, false);
        setStyle();
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = new EmojiCommentModel(mBuilder);
        mDataBinding.setModel(mModel);

        mDataBinding.emojiListView.setOnItemClickListner(emoji -> mDataBinding.emojiEditText.addEmoji(emoji));
        mDataBinding.iconImg.setOnClickListener(v -> toggleImgVisibility());
        mDataBinding.submitBtn.setOnClickListener(v -> onPublishBtnClicked());

        mDataBinding.emojiEditText.setOnClickListener(v -> {
            if (mModel.isEmojiListVisible()) mModel.toggleEmojiListVisibile();
        });
        mDataBinding.emojiEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onPublishBtnClicked();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataBinding.emojiEditText.requestFocus();
        KeyBoardUtils.showSoftInput(mDataBinding.emojiEditText);
    }

    /**
     * 选择表情/输入文字切换
     */
    private void toggleImgVisibility() {
        mModel.toggleEmojiListVisibile();
        if (mModel.isEmojiListVisible()) {
            KeyBoardUtils.hideSoftInput(mDataBinding.emojiEditText);
        } else {
            KeyBoardUtils.showSoftInput(mDataBinding.emojiEditText);
        }
    }

    private void onPublishBtnClicked() {
        if (mBuilder.publishBtnClickListener != null) {
            boolean dissmiss =
                    mBuilder.publishBtnClickListener.onPublishBtnClicked(this,
                            mDataBinding.emojiEditText.getEmojiText());
            if (dissmiss) {
                dismiss();
            }
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        KeyBoardUtils.hideSoftInput(mDataBinding.emojiEditText);
        super.dismiss();
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
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent, null)));
        } else {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
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

    @SuppressWarnings("unused")
    public static class Builder {
        int width = WindowManager.LayoutParams.MATCH_PARENT;

        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        int gravity = Gravity.BOTTOM;

        int publishBtnTextSize = TransformUtils.dip2px(13);

        @StyleRes
        int theme = R.style.BaseEmojiDialog;

        @StyleRes
        int animation = R.style.PopStyle;

        @ColorInt
        int publishBtnNormalColorResource = Color.parseColor("#E9EBEC");

        @ColorInt
        int publishBtnActiveColorResource = Color.parseColor("#FF14A5E2");

        String hint;

        OnPublishBtnClickListener publishBtnClickListener;

        public EmojiCommentDialog build() {
            return new EmojiCommentDialog(this);
        }

        public void show(FragmentManager fragmentManager) {
            build().show(fragmentManager, null);
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setAnimation(int animation) {
            this.animation = animation;
            return this;
        }

        public Builder setPublishBtnTextSize(int dpValue) {
            this.publishBtnTextSize = TransformUtils.dip2px(dpValue);
            return this;
        }

        public Builder hint(String hint) {
            this.hint = hint;
            return this;
        }

        public String getHint() {
            return hint;
        }

        /**
         * 设置Button背景颜色样式
         *
         * @param normalColor 空文本时的颜色  传{@code -1} 即使用默认颜色
         * @param activeColor 有文本时的颜色
         */
        public Builder setPublishBtnColorResource(@ColorInt int normalColor, @ColorInt int activeColor) {
            this.publishBtnNormalColorResource = normalColor != -1 ? normalColor : publishBtnNormalColorResource;
            this.publishBtnActiveColorResource = activeColor != -1 ? activeColor : publishBtnActiveColorResource;
            return this;
        }

        /**
         * 发布按钮点击事件
         */
        public Builder setPublishBtnClickListener(OnPublishBtnClickListener publishBtnClickListener) {
            this.publishBtnClickListener = publishBtnClickListener;
            return this;
        }

        public int getPublishBtnTextSize() {
            return publishBtnTextSize;
        }

        public int getPublishBtnNormalColorResource() {
            return publishBtnNormalColorResource;
        }

        public int getPublishBtnActiveColorResource() {
            return publishBtnActiveColorResource;
        }

        public interface OnPublishBtnClickListener {
            /**
             * @return 是否{@link #dismiss()}
             */
            boolean onPublishBtnClicked(EmojiCommentDialog dialog, @Nullable String content);
        }
    }
}
