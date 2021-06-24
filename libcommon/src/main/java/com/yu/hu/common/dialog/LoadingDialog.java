package com.yu.hu.common.dialog;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.yu.hu.common.R;
import com.yu.hu.common.databinding.DialogLoadingBinding;

/**
 * Created by Hy on 2019/11/22 12:51
 **/
@SuppressWarnings("unused")
public class LoadingDialog extends BaseDialog<LoadingDialog, DialogLoadingBinding> {

    /**
     * progressBar的颜色
     */
    @ColorRes
    private int progressBarColorRes;

    public static LoadingDialog newInstance() {
        return new LoadingDialog();
    }

    private LoadingDialog() {
        //初始化值
        this.contentColorRes = R.color.colorPrimary;
        this.progressBarColorRes = R.color.colorPrimary;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mDataBinding.setLoadingText(content);
        mDataBinding.tvContent.setTextColor(getResources().getColor(contentColorRes));
        setProgressBarColor();
    }

    //api 21  Android 5.0 及以上才有
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setProgressBarColor() {
        //状态
        int[][] states = new int[2][];
        //按下
        states[0] = new int[]{android.R.attr.state_pressed};
        //默认
        states[1] = new int[]{};

        int barColor = getResources().getColor(progressBarColorRes);
        //状态对应颜色值（按下，默认）
        int[] colors = new int[]{barColor, barColor};
        ColorStateList colorList = new ColorStateList(states, colors);

        mDataBinding.progressBar.setIndeterminateTintList(colorList);
    }

    /* *******************对外暴露的方法******************** */

    /**
     * 设置提示内容
     *
     * @param contentRes StringRes
     */
    @Override
    public LoadingDialog setContent(@StringRes int contentRes) {
        return super.setContent(contentRes);
    }

    /**
     * 设置提示内容
     *
     * @param content content 如果为空或空串则自动隐藏
     * @see com.yu.hu.common.binding.BindingAdapter#setText(TextView, String)
     */
    @Override
    public LoadingDialog setContent(@Nullable String content) {
        return super.setContent(content);
    }

    public void changeHint(String hint) {
        if (mDataBinding != null) {
            mDataBinding.tvContent.setText(hint);
        }
    }

    /**
     * 统一设置progressBar及文字的颜色
     *
     * @param displayColor color
     */
    public LoadingDialog setDisplayColorResource(@ColorRes int displayColor) {
        this.contentColorRes = displayColor;
        this.progressBarColorRes = displayColor;
        return this;
    }

    /**
     * content颜色
     *
     * @param contentColor color
     */
    public LoadingDialog setContentColorResource(@ColorRes int contentColor) {
        this.contentColorRes = contentColor;
        return this;
    }

    /**
     * progressBar 颜色
     *
     * @param progressBarColor color
     */
    public LoadingDialog setProgressBarColorResource(@ColorRes int progressBarColor) {
        this.progressBarColorRes = progressBarColor;
        return this;
    }

}
