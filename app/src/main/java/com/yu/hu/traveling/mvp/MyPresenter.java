package com.yu.hu.traveling.mvp;

import android.content.Context;
import android.util.AttributeSet;

import com.yu.hu.library.mvp.BasePresenter;
import com.yu.hu.traveling.mvp.impl.MyPrensence;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.mvp
 * 文件名：MyPresenter
 * 创建者：HY
 * 创建时间：2019/7/9 9:41
 * 描述：  TODO
 */
public class MyPresenter extends BasePresenter<MyPrensence> {


    public MyPresenter(MyPrensence impl) {
        super(impl);
    }
}
