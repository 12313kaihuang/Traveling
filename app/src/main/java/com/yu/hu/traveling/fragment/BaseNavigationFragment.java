package com.yu.hu.traveling.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.common.utils.LogUtil;

/**
 * @author Hy
 * created on 2020/05/08 10:48
 * <p>
 * 为了处理navigation返回时重新出发onCreateView onViewCreated操作
 **/
@SuppressWarnings("unused")
public abstract class BaseNavigationFragment<D extends ViewDataBinding> extends BaseFragment<D> {

    private static final String TAG = "BaseNavigationFragment";

    private boolean isViewInit = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mDataBinding == null) {
            isViewInit = false;
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        LogUtil.d(generateLogTag(), "onCreateView - 已保存无需重新创建");
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (!isViewInit) {
            super.onActivityCreated(savedInstanceState);
            isViewInit = true;
        } else {
            superActivityCreated(savedInstanceState);
            LogUtil.d(generateLogTag(), "onViewCreated - hasCreate");
        }
    }
}
