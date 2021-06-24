package com.yu.hu.common.activity;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.yu.hu.common.utils.NavOptionsBuilder;

/**
 * @author Hy
 * created on 2020/04/14 11:53
 * <p>
 * 带有navigation的Activity
 * 即布局中含navHostFragment的Activity
 **/
@SuppressWarnings("unused")
public abstract class BaseNavigationActivity<D extends ViewDataBinding> extends BaseActivity<D> {

    protected NavController mNavController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavController = Navigation.findNavController(this, getNavHostFragmentId());
    }

    /**
     * @return NavHostFragment的id
     */
    @IdRes
    protected abstract int getNavHostFragmentId();

    public NavController getNavController() {
        return mNavController;
    }

    /**
     * 简化getNavController().navigate()
     */
    public void navigate(int destId) {
        mNavController.navigate(destId);
    }

    public void navigate(int destId, NavOptions navOptions) {
        mNavController.navigate(destId,null,navOptions);
    }

    /**
     * 简化getNavController().popBackStack()
     */
    public void popBackStack(){
        mNavController.popBackStack();
    }
}
