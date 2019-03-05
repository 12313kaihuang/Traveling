package com.android.traveling.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.widget
 * 文件名：ToLogoutDialog
 * 创建者：HY
 * 创建时间：2019/3/5 18:12
 * 描述：  确认退出按钮
 */

public class ToLogoutDialog extends ToLoginDialog {

    private OnLogoutListener onLogoutListener;

    public ToLogoutDialog(Context context, OnLogoutListener listener) {
        this(context, "确定要退出登录吗？");
        this.onLogoutListener = listener;
    }

    private ToLogoutDialog(@NonNull Context context, String hint) {
        super(context, hint);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_to_login.setText("确定");
        tv_to_login.setTextColor(getContext().getResources().getColor(R.color.colorblue_btn));
        tv_to_login.setOnClickListener(v -> {
            onLogoutListener.onLogout();
            dismiss();
        });
    }

    public interface OnLogoutListener {
        void onLogout();
    }
}
