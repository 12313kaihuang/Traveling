package com.yu.hu.traveling.widget;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 项目名：SearchView
 * 包名：  com.searchview
 * 文件名：ISearcher
 * 创建者：HY
 * 创建时间：2019/1/31 14:25
 * 描述：  SearchView功能相关接口
 */

@SuppressWarnings("unused")
public interface ISearcher {

    /**
     * 搜索图标按钮点击事件
     *
     * @param listener 回调接口listener
     */
    void setImageButtonSearchClickListener(onImageButtonSearchClickListener listener);
/**
     * 语音按钮点击事件
     *
     * @param listener 回调接口listener
     */
    void setImageButtonVoiceClickListener(onImageButtonVoiceClickListener listener);

    /**
     * 清除图标点击事件
     *
     * @param listener 回调接口listener
     */
    void setImageButtonCancelClickListener(onImageButtonCancelClickListener listener);

    /**
     * “搜索”点击事件
     *
     * @param listener 回调接口listener
     */
    void setonTextViewSearchClickListener(onTextViewSearchClickListener listener);

    /**
     * 获取EditText的内容
     *
     * @return EditText的内容
     */
    String getSearchContent();

    /**
     * 设置EditText的内容
     *
     * @param content content
     */
    void setSearchContent(String content);

    /**
     * 获取EditText
     *
     * @return EditText
     */
    EditText getEt_input();

    /**
     * 设置搜索图标
     *
     * @param searchIcon searchIcon
     */
    void setSearchIcon(Drawable searchIcon);

    /**
     * 设置语音图标
     *
     * @param voiceIcon voiceIcon
     */
    void setVoiceIcon(Drawable voiceIcon);

    /**
     * 设置清除图标
     *
     * @param clearIcon clearIcon
     */
    void setClearIcon(Drawable clearIcon);

    /**
     * 搜索按钮点击回调接口
     */
    interface onImageButtonSearchClickListener {
        void onClick(EditText input, ImageView search, View view);
    }

    /**
     * 语音按钮点击回调接口
     */
    interface onImageButtonVoiceClickListener {
        void onClick(EditText input, ImageView voice, View view);
    }

    /**
     * 清除按钮点击回调接口
     */
    interface onImageButtonCancelClickListener {
        void onClick(EditText input, ImageView cancel, View view);
    }

    /**
     * 点击搜索回调接口
     */
    interface onTextViewSearchClickListener {
        void onClick(EditText input, TextView search, View view);
    }

}

