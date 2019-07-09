package com.yu.hu.library.widget.dialog;

import android.app.Dialog;

/**
 * 文件名：LifeCycleListener
 * 创建者：HY
 * 创建时间：2019/6/26 22:29
 * 描述：  dialog生命周期监听
 */
public interface LifeCycleListener {

    /**
     * 创建  用于对界面上的一些控件做处理
     * 此处dialog可以强转为对应的dialog
     */
    default void onCreated(Dialog dialog) {

    }

    /**
     * 取消
     */
    default void onCanceled(Dialog dialog) {

    }

}
