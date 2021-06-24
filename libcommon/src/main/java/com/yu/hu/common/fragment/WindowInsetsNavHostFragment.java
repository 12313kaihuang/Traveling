package com.yu.hu.common.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.yu.hu.common.utils.LogUtil;

/**
 * 可替换NavHostFragment
 * 使沉浸式布局中fitsSystemWindows对所有Fragment都有用
 * 默认NavHostFragment中，只有第一个Fragment设置该属性才有用
 */
@SuppressWarnings("unused")
public class WindowInsetsNavHostFragment extends NavHostFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        WindowInsetsFrameLayout layout = new WindowInsetsFrameLayout(inflater.getContext());
        layout.setId(getId());
        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getChildFragmentManager().getPrimaryNavigationFragment();
        if (fragment != null) {
            LogUtil.d("onActivityResult - " + fragment.getClass().getSimpleName());
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
