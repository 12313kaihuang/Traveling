package com.android.traveling.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget
 * 文件名：MyActionBar
 * 创建者：HY
 * 创建时间：2019/3/8 15:01
 * 描述：  自定义 ActionBar my_fragment中的
 */

public class MyActionBar2 extends LinearLayout {

    private LinearLayout ll_bg;
    private  ImageView right_img;
    private  ImageView left_img;
    private TextView title;


    public MyActionBar2(@NonNull Context context) {
        this(context, null);
    }

    public MyActionBar2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyActionBar2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.my_action_bar2, this);
        ll_bg = view.findViewById(R.id.ll_bg);
        right_img = view.findViewById(R.id.iv_right);
        left_img = view.findViewById(R.id.iv_left);
        title = view.findViewById(R.id.tv_username);
        ll_bg.getBackground().setAlpha(0);
    }

    /**
     * 改变透明度
     *
     * @param alpha alpha
     */
    public void changeAlpha(float alpha) {
        if (alpha < 0.5f) {
            setRightImg(R.drawable.ic_sort);
            left_img.setImageResource(R.drawable.ic_share);
        } else {
            setRightImg(R.drawable.ic_sort_black);
            left_img.setImageResource(R.drawable.ic_share_black);
        }
        ll_bg.getBackground().setAlpha((int) (alpha * 255));
        title.setAlpha(alpha);
    }

    /**
     * 获取TextView
     *
     * @return userName
     */
    public TextView getTitle() {
        return title;
    }

    /**
     * 获取ImageView
     *
     * @return iv_back
     */
    @SuppressWarnings("unused")
    public ImageView getRightImg() {
        return right_img;
    }

    //添加点击事件
    public void setOnClickListener(onClickListener clickListener) {
        right_img.setOnClickListener(clickListener::onRightClick);
        left_img.setOnClickListener(clickListener::onLeftClick);
    }

    /**
     * 设置标题
     *
     * @param text text
     */
    public void setTitle(String text) {
        title.setText(text);
    }

    /**
     * 设置标题同时设置颜色
     *
     * @param text  text
     * @param color color
     */
    @SuppressWarnings("unused")
    public void setTitle(String text, @ColorInt int color) {
        title.setText(text);
        title.setTextColor(color);
    }

    /**
     * 设置返回按钮的样式资源
     *
     * @param resId resId
     */
    public void setRightImg(@DrawableRes int resId) {
        right_img.setImageResource(resId);
    }

    /**
     * 按钮点击回调接口
     */
    public interface onClickListener {
        void onRightClick(View v);

        void onLeftClick(View v);
    }
}
