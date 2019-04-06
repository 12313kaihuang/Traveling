package com.android.traveling.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget
 * 文件名：MyActionBar
 * 创建者：HY
 * 创建时间：2019/3/8 15:01
 * 描述：  自定义 ActionBar
 */

@SuppressWarnings("unused")
public class MyActionBar extends FrameLayout {

    View view;
    ImageView iv_back;
    TextView userName;

    public MyActionBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.my_action_bar,this);
        iv_back = view.findViewById(R.id.iv_back);
        userName = view.findViewById(R.id.tv_username);
    }

    /**
     * 获取TextView
     * @return userName
     */
    public TextView getTVTitle(){
        return userName;
    }

    /**
     * 获取ImageView
     * @return iv_back
     */
    public ImageView getIvBack(){
        return iv_back;
    }

    /**
     * 设置返回按钮点击事件
     * @param listener listener
     */
    public void setOnBackClickListener(View.OnClickListener listener) {
        iv_back.setOnClickListener(listener);
    }

    /**
     * 设置标题
     * @param text  text
     */
    public void setActionBarTitle(String text) {
        userName.setText(text);
    }

    /**
     * 设置标题同时设置颜色
     * @param text text
     * @param color color
     */
    public void setActionBarTitle(String text,@ColorInt int color) {
        userName.setText(text);
        userName.setTextColor(color);
    }

    /**
     * 设置返回按钮的样式资源
     * @param resId resId
     */
    public void setBackImg(@DrawableRes int resId){
        iv_back.setImageResource(resId);
    }
}
