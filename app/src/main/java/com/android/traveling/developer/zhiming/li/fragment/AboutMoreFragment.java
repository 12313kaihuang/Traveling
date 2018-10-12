package com.android.traveling.developer.zhiming.li.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.zhiming.li.MyFragment;
import com.android.traveling.developer.zhiming.li.ui.LoginActivity;
import com.android.traveling.developer.zhiming.li.ui.UserEditActivity;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.avos.avoscloud.AVUser;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.fragment
 * 文件名：AboutMoreFragment
 * 创建者：HY
 * 创建时间：2018/10/12 18:25
 * 描述：  关于更多侧滑菜单
 */

public class AboutMoreFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_more, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView about_edit = view.findViewById(R.id.about_edit);
        TextView about_logout = view.findViewById(R.id.about_logout);
        TextView about_bind = view.findViewById(R.id.about_bind);
        TextView about_privacy = view.findViewById(R.id.about_privacy);
        about_edit.setOnClickListener(this);
        about_logout.setOnClickListener(this);
        about_bind.setOnClickListener(this);
        about_privacy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_edit:
                if (AVUser.getCurrentUser() != null) {
                    startActivity(new Intent(getActivity(), UserEditActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.about_bind:
                if (AVUser.getCurrentUser() != null) {
                    UtilTools.toast(getContext(), "账号绑定");
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.about_privacy:
                if (AVUser.getCurrentUser() != null) {
                    UtilTools.toast(getContext(), "隐私设置");
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.about_logout:

                if (AVUser.getCurrentUser() != null) {

                    //noinspection ConstantConditions
                    new AlertDialog.Builder(getContext())
                            .setMessage("确定要退出登录吗？")
                            .setPositiveButton("确定", (dialog, which) -> {
                                AVUser.logOut();
                                UtilTools.toast(getContext(), "已退出当前用户");
                            })
                            .setNegativeButton("取消", (dialog, which) -> {

                            }).show();

                } else {
                    UtilTools.toast(getContext(), "退出失败，当前是未登录状态");
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("AboutMoreFragment onDestroy");
    }
}
