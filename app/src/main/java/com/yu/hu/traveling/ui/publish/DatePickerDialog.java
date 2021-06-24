package com.yu.hu.traveling.ui.publish;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.traveling.databinding.DialogDatePickerBinding;

import java.util.Calendar;

/**
 * @author Hy
 * created on 2020/05/06 21:17
 * <p>
 * 日期选择框
 * 参考 https://www.jianshu.com/p/6700e0422e6e
 **/
public class DatePickerDialog extends DialogFragment implements DatePicker.OnDateChangedListener {

    private static final long DAY_TIME_MILLS = 1000 * 60 * 60 * 24;
    private static final long MONTH_TIME_MILLS = DAY_TIME_MILLS * 30;

    private Builder mBuilder;
    private DialogDatePickerBinding mDataBinding;
    private Calendar startDate;
    private Calendar endDate;

    @SuppressWarnings("WeakerAccess")
    public DatePickerDialog(@NonNull Builder builder) {
        this.mBuilder = builder;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DialogDatePickerBinding.inflate(inflater, container, false);
        setStyle();
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatePicker datePicker = mDataBinding.datePickerView;
        mDataBinding.backBtn.setOnClickListener(v -> dismiss());
        mDataBinding.ensureBtn.setOnClickListener(v -> returnSelectedDateUnderLOLLIPOP());

        //如果只要日历部分，隐藏header
        ViewGroup mContainer = (ViewGroup) datePicker.getChildAt(0);
        View header = mContainer.getChildAt(0);
        header.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Marshmallow上底部留白太多，减小间距
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) datePicker.getLayoutParams();
            layoutParams.bottomMargin = 10;
            datePicker.setLayoutParams(layoutParams);
        }
        initDatePicker();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { //LOLLIPOP上，这个回调无效，排除将来可能的干扰
            return;
        }
        if (mBuilder.listener != null) {
            String date = getDate(year, monthOfYear, dayOfMonth);
            mBuilder.listener.onDateSelect(date);
        }
        dismiss();
    }

    private void initDatePicker() {
        startDate = Calendar.getInstance();
        startDate.setTimeInMillis(System.currentTimeMillis());
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        mDataBinding.datePickerView.init(startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH), this);
        //可选择的最早时间
        mDataBinding.datePickerView.setMinDate(startDate.getTimeInMillis());

        //6.0+上bug：初始化时会触发一次onDateChanged回调。通过源码分析一下原因：init方法只会设置控件当前日期的
        //年月日，而时分秒默认使用现在时间的时分秒，所以当前日期大于>最大日期，执行setMaxDate方法时，就会触发一次onDateChanged回调。
        //同理，setMinDate方法也面临同样的方法。所以设置范围时，MinDate取0时0分0秒，MaxDate取23时59分59秒。
        endDate = Calendar.getInstance();
        endDate.setTimeInMillis(startDate.getTimeInMillis() + MONTH_TIME_MILLS * 3);
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        //可选择最晚时间 可选择3个月内的时间
        mDataBinding.datePickerView.setMaxDate(endDate.getTimeInMillis());
    }

    private void returnSelectedDateUnderLOLLIPOP() {
        //bug3:5.0上超过可选区间的日期依然能选中,所以要手动校验.5.1上已解决，但是为了与5.0保持一致，也采用确定菜单返回日期
        DatePicker datePicker = mDataBinding.datePickerView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 0, 0, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);
            if (selectedDate.before(startDate) || selectedDate.after(endDate)) {
                ToastUtils.showShort("日期超出有效范围");
                return;
            }
        }
        if (mBuilder.listener != null) {
            String date = getDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            mBuilder.listener.onDateSelect(date);
        }
        dismiss();
    }

    @SuppressLint("DefaultLocale")
    private String getDate(int year, int month, int dayOfMonth) {
        return String.format("%d-%d-%d", year, month + 1, dayOfMonth);
    }

    /**
     * 有关style的一些初始化操作
     * 如果设置背景透明等
     */
    private void setStyle() {
        //无title  自定义主题
        setStyle(DialogFragment.STYLE_NO_TITLE, mBuilder.theme);

        Dialog dialog = getDialog();
        if (dialog == null) {
            Log.w(getClass().getSimpleName(), "dialog == null");
            return;
        }

        //dialog无title
        dialog.requestWindowFeature(STYLE_NO_TITLE);

        Window window = dialog.getWindow();
        if (window == null) {
            Log.w(getClass().getSimpleName(), "dialog.window == null");
            return;
        }

        //透明背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.yu.hu.emoji.R.color.transparent, null)));
        } else {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.yu.hu.emoji.R.color.transparent)));
        }

        //设置宽高
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = mBuilder.width;
        attributes.height = mBuilder.height;
        attributes.gravity = mBuilder.gravity;
        //设置动画
        window.setWindowAnimations(mBuilder.animation);
        window.setAttributes(attributes);
    }

    public static class Builder {

        int width = WindowManager.LayoutParams.MATCH_PARENT;

        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        int gravity = Gravity.CENTER;

        @StyleRes
        int theme = com.yu.hu.emoji.R.style.BaseEmojiDialog;

        @StyleRes
        int animation = com.yu.hu.emoji.R.style.PopStyle;

        OnDateSelectedListener listener;

        @SuppressWarnings("WeakerAccess")
        public Builder setOnDateSelectedListener(OnDateSelectedListener listener) {
            this.listener = listener;
            return this;
        }

        public DatePickerDialog build() {
            return new DatePickerDialog(this);
        }

        public void show(FragmentManager fragmentManager) {
            build().show(fragmentManager, null);
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelect(String date);
    }
}
