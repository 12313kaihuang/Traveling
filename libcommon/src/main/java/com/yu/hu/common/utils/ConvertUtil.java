package com.yu.hu.common.utils;

import android.content.Context;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * Created by Hy on 2019/11/16 20:31
 * <p>
 * 转换相关util，
 * 复现一遍加深印象
 *
 * @see com.blankj.utilcode.util.ConvertUtils
 **/
@SuppressWarnings("unused")
public class ConvertUtil {

    /**
     * Value of sp to value of px.
     *
     * @param spValue The value of sp.
     * @return value of px
     * @see ConvertUtils#sp2px(float)
     */
    public static int sp2px(Context context, final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Value of dp to value of px.
     *
     * @param dpValue The value of dp.
     * @return value of px
     * @see ConvertUtils#dp2px(float)
     */
    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
