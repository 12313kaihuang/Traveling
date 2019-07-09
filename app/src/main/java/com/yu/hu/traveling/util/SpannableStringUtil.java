package com.yu.hu.traveling.util;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.yu.hu.traveling.R;

import androidx.annotation.NonNull;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.util
 * 文件名：SpannableStringUtil
 * 创建者：HY
 * 创建时间：2019/2/28 15:10
 * 描述：  SpannableString 工具类
 */

public class SpannableStringUtil {

    /**
     * 评论/回复 那里用(蓝色的可点击用户名)
     * @param mClickableSpan 自定义接口
     * @return ClickableSpan
     */
    public static ClickableSpan getClickableReplySpan(Context context, mClickableSpan mClickableSpan) {
        return new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                mClickableSpan.onClick(widget);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.blue_comment));   //设置字体颜色
                ds.setUnderlineText(false);    //设置是否显示下划线
                ds.clearShadowLayer();   //阴影
            }
        };
    }

    //自定义接口
    public interface mClickableSpan {
        void onClick(View widget);
    }
}
