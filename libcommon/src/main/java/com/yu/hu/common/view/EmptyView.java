package com.yu.hu.common.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.ConvertUtils;
import com.yu.hu.common.databinding.LayoutEmptyViewBinding;
import com.yu.hu.common.utils.LogUtil;

/**
 * @author Hy
 * created on 2020/04/18 13:23
 * <p>
 * 空布局 + progressBar
 **/
@SuppressWarnings("unused")
public class EmptyView extends FrameLayout {
    private LayoutEmptyViewBinding mDataBinding;

    public EmptyView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        //setBackgroundColor(getResources().getColor(R.color.white));
        mDataBinding = LayoutEmptyViewBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setBackgroundResource(@ColorRes int color) {
        mDataBinding.getRoot().setBackgroundResource(color);
    }

    /**
     * ProgressBar可见性
     */
    public void setProgressBarVisibility(boolean isVisible) {
        mDataBinding.emptyView.setVisibility(!isVisible ? VISIBLE : GONE);
        mDataBinding.progressBar.setVisibility(isVisible ? VISIBLE : GONE);
    }

    /**
     * EmptyView可见性
     */
    public void setEmptyViewVisibility(boolean isVisible) {
        mDataBinding.progressBar.setVisibility(!isVisible ? VISIBLE : GONE);
        mDataBinding.emptyView.setVisibility(isVisible ? VISIBLE : GONE);
    }

    /**
     * 设置空布局的icon
     */
    public void setEmptyIcon(@DrawableRes int iconRes) {
        mDataBinding.emptyIcon.setImageResource(iconRes);
    }

    public void setTitle(@StringRes int title) {
        setTitle(getContext().getResources().getString(title));
    }

    /**
     * 设置空布局的提示文本
     */
    public void setTitle(String text) {
        if (TextUtils.isEmpty(text)) {
            mDataBinding.emptyText.setVisibility(GONE);
        } else {
            mDataBinding.emptyText.setText(text);
            mDataBinding.emptyText.setVisibility(VISIBLE);
        }

    }

    /**
     * 设置空布局的按钮文本及点击事件
     */
    public void setButton(String text, View.OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            mDataBinding.refreshBtn.setVisibility(GONE);
        } else {
            mDataBinding.refreshBtn.setText(text);
            mDataBinding.refreshBtn.setVisibility(VISIBLE);
            mDataBinding.refreshBtn.setOnClickListener(listener);
        }
    }
}
