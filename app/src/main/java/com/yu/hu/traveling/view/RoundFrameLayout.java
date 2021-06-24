package com.yu.hu.traveling.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yu.hu.common.view.ViewHelper;

/**
 * @author Hy
 * created on 2020/04/09 10:36
 **/
public class RoundFrameLayout extends FrameLayout {
    public RoundFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressLint("NewApi")
    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ViewHelper.setViewOutLine(this, attrs, defStyleAttr, defStyleRes);
    }

    public void setViewOutLine(int radius, int radiusSide) {
        ViewHelper.setViewOutLine(this, radius, radiusSide);
    }
}
