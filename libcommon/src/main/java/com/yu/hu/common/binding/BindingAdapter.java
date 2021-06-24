package com.yu.hu.common.binding;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * create by hy on 2019/11/23 14:03
 * <p>
 * 使用BindAdapter更好的实现一些UI更改
 *
 * @see androidx.databinding.BindingAdapter
 */
public class BindingAdapter {

    /**
     * 设置内容，
     * 如果内容为空则隐藏
     *
     * @param textView textView
     * @param text text
     */
    @androidx.databinding.BindingAdapter("text")
    public static void setText(TextView textView, String text) {
        textView.setText(text);
        textView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

}
