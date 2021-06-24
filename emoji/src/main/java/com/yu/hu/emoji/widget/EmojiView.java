package com.yu.hu.emoji.widget;


import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

import com.yu.hu.emoji.utils.TransformUtils;

/**
 * Created by Hy on 2019/12/30 18:05
 * <p>
 * 对应一个小表情
 **/
public class EmojiView extends AppCompatImageView {

    //图标大小
    private static final int DEFAULT_SIZE = 28;

    private int size = DEFAULT_SIZE;

    public EmojiView(Context context) {
        super(context);
    }

    /**
     * 设置表情大小
     *
     * @param size 大小 dp
     */
    @SuppressWarnings("unused")
    public EmojiView setSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, TransformUtils.dip2px(getContext(), size));
    }
}
