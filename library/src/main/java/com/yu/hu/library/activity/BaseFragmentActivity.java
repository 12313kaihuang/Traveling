package com.yu.hu.library.activity;

import android.os.Bundle;

import com.yu.hu.library.R;
import com.yu.hu.library.fragment.BaseFragment;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

/**
 * 文件名：BaseActivity
 * 创建者：HY
 * 创建时间：2019/4/20 9:09
 * 描述：  BaseFragmentActivity
 * 布局相对复杂，需要嵌套Fragment的Activity
 * 有一个默认的布局样式{@code R.layout.activity_base}
 * 其中包裹Fragment的containerId默认为{@code R.id.fragment_container}，可自己重写
 * <p>
 * 必须重写{@link #buildFragment()}方法
 *
 * @see #getLayoutId() 布局文件id
 * @see #buildFragment() 构建Fragment
 * @see #replaceFragment(int, BaseFragment)
 * @see #getContentFragmentId() 布局文件中放Fragment的那个id
 * @see #getCurrentFragment()  获取当前显示的Fragment
 */
public abstract class BaseFragmentActivity extends BaseActivity {

    /**
     * 当前所显示的fragment
     */
    private BaseFragment mCurrentFragment;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        initFragment();
    }

    /**
     * 设置fragment
     */
    private void initFragment() {
        replaceFragment(getContentFragmentId(), buildFragment());
    }

    @SuppressWarnings("unused")
    protected final <T extends BaseFragment> void replaceFragment(T fragment) {
        replaceFragment(getContentFragmentId(), fragment);
    }

    /**
     * 设置fragment
     *
     * @param containerId containerId
     * @param fragment    fragment extends BaseFragment
     * @param <T>         T
     */
    protected final <T extends BaseFragment> void replaceFragment(@IdRes int containerId, T fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mCurrentFragment = fragment;
        transaction.replace(containerId, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 加载所要显示的 fragment
     */
    protected abstract BaseFragment buildFragment();


    /**
     * 有一个默认的布局样式
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    /**
     * 所包含的fragment布局文件的id
     * <p>
     * 可以重写 默认为 R.id.fragment_container
     */
    @IdRes
    public int getContentFragmentId() {
        return R.id.fragment_container;
    }

    /**
     * 获取当前显示的fragment
     */
    @SuppressWarnings({"unchecked", "unused"})
    public final <T extends BaseFragment> T getCurrentFragment() {
        return (T) mCurrentFragment;
    }

}
