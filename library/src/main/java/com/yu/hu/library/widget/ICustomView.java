package com.yu.hu.library.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 文件名：ICustomView
 * 创建者：HY
 * 创建时间：2019/6/11 10:30
 * 描述：  自定义View继承此接口
 */
public interface ICustomView {


    /**
     * 初始化操作
     * 初始化成员变量等
     * 在构造函数中用吧
     */
    void initView(Context context);

    /**
     * 初始化自定义属性
     * 在构造函数中调用吧
     */
    default void initParams(Context context, AttributeSet attrs) {
        //inflate(context, R.layout.button_select_layout, this);
        //mView = LayoutInflater.from(context).inflate(R.layout.button_select_layout, this);
        //TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonSelectView);
    }
}
