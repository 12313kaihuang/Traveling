package com.yu.hu.library.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.yu.hu.library.R;
import com.yu.hu.library.fragment.BaseFragment;
import com.yu.hu.library.util.FragmentFactory;

import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.yu.hu.library.util.LogUtil;


/**
 * 文件名：BaseNavigationActivity
 * 创建者：HY
 * 创建时间：2019/6/21 17:16
 * 描述：  带有底部NavigationBar的Activity
 * 有一个默认的布局样式{@code R.layout.activity_nav}
 * 必须包含一个{@link JPTabBar}且id为{@code R.id.bottom_nav_bar}
 * 其中包裹Fragment的containerId默认为{@code R.id.fragment_container}，可自己重写
 * <p>
 * 子类如果需要重写
 * <p>
 * 核心的两个方法：
 * {@link #getItemList()}
 * {@link #onTabSelected(BottomNavItem)}
 *
 * @see BottomNavItem
 * @see #needCache() 是否缓存fragment
 * @see #onTabSelected(BottomNavItem)  点击事件
 * @see #getContentFragmentId() 布局文件中放Fragment的那个id
 * @see #getDefaultSelectedPosition()  默认0
 * @see #getItemList() 必须重写
 * @see #getBarBackgroundColor()
 * @see #getNormalColor()
 * @see #getActiveColor()
 */
@SuppressWarnings("unused")
public abstract class BaseNavigationActivity extends BaseFragmentActivity implements OnTabSelectListener {

    //https://www.jianshu.com/p/c9c7747768d0
    /**
     * NavigationBar
     */
    protected JPTabBar mNavBar;

    /**
     * 底部图标组
     * 自定义类
     */
    protected List<BottomNavItem> mItemList;

    //存储当前显示fragment的下标
    private int currentIndex;

    /**
     * 有一个默认的布局样式
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_nav;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        mItemList = getItemList();
        currentIndex = getDefaultSelectedPosition();
        mNavBar = findViewById(R.id.bottom_nav_bar);

        //检测下标是否越界
        checkIndex(getDefaultSelectedPosition());

        //初始化navigationBar
        initNavBar();

        changeCurrentFragment(getDefaultSelectedPosition());
    }

    /**
     * navigationBar的一些初始化操作
     */
    private void initNavBar() {

        LogUtil.d("mItemList.size() = " + mItemList.size());

        mNavBar.setTitles(getTitles())
                .setNormalIcons(getNormalIcons())
                .setSelectedIcons(getSelectedIcons())
                .generate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mNavBar.setNormalColor(getResources().getColor(getNormalColor(), null));
            mNavBar.setSelectedColor(getResources().getColor(getActiveColor(), null));
            mNavBar.setBackgroundColor(getResources().getColor(getBarBackgroundColor(), null));
        } else {
            mNavBar.setNormalColor(getResources().getColor(getNormalColor()));
            mNavBar.setSelectedColor(getResources().getColor(getActiveColor()));
            mNavBar.setBackgroundColor(getResources().getColor(getBarBackgroundColor()));
        }

        mNavBar.setSelectTab(getDefaultSelectedPosition());
        mNavBar.setTabListener(this);
        View middleView = mNavBar.getMiddleView();
        if (middleView != null) {
            middleView.setOnClickListener(this::onNavBarMidViewClick);
        }
    }

    /**
     * 切换当前显示的fragment
     * 隐藏与切换本身不会调用fragment所对应的onResume和onPause方法，所以这里手动调用一下
     *
     * @param index 下标
     */
    protected final void changeCurrentFragment(int index) {
        //检测下标是否越界
        checkIndex(index);
        LogUtil.d("changeCurrentFragment -- currentIndex = " + currentIndex + ",targetIndex = " + index);

        if (!needCache()) {
            //不需要缓存
            BaseFragment fragment = FragmentFactory.createFragment(this, mItemList.get(index).fragmentName);
            replaceFragment(getContentFragmentId(), fragment);
            return;
        }

        //需要切换为显示状态的fragment
        Fragment targetFragment = findFragment(index);
        //正在显示的fragment
        Fragment currentFragment = findFragment(currentIndex);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .hide(currentFragment);
        if (targetFragment.isAdded()) {
            //之前添加过就直接show
            LogUtil.d("showFragment -- " + targetFragment.getClass().getSimpleName());
            transaction.show(targetFragment);
            LogUtil.d("BaseNavigationActivity 调用了 -- " + targetFragment.getClass().getSimpleName() + ".onResume");
            targetFragment.onResume();
        } else {
            //没有添加过就添加进去
            LogUtil.d("addFragment -- " + targetFragment.getClass().getSimpleName());
            transaction.add(getContentFragmentId(), targetFragment, String.valueOf(index));
        }
        currentIndex = index;
        transaction.commit();

        if (currentFragment.isAdded()) {
            LogUtil.d("BaseNavigationActivity 调用了 -- " + currentFragment.getClass().getSimpleName() + ".onPause");
            currentFragment.onPause();
        }
    }

    /**
     * 通过index对应一个tag查找fragment，
     * 如果没有就新建一个
     *
     * @param index 下标
     */
    private Fragment findFragment(int index) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(index));
        if (fragment == null) {
            return FragmentFactory.createFragment(this, mItemList.get(index).fragmentName);
        }
        return fragment;
    }

    /**
     * 是否需要缓存fragment
     *
     * @return true -> NavigationBar切换fragment时会缓存上一个fragment
     */
    protected boolean needCache() {
        return true;
    }

    /**
     * 设置默认选中的item位置
     */
    public int getDefaultSelectedPosition() {
        return 0;
    }

    /**
     * 设置item列表
     */
    public abstract List<BottomNavItem> getItemList();

    /**
     * @return navigationBar的背景颜色
     */
    @ColorRes
    public int getBarBackgroundColor() {
        return R.color.white;
    }

    /**
     * @return item未选中icon、title颜色
     */
    @ColorRes
    public int getNormalColor() {
        return R.color.black;
    }

    /**
     * @return item选中icon、title颜色
     */
    @ColorRes
    public int getActiveColor() {
        return R.color.colorPrimary;
    }

    @Override
    public final void onTabSelect(int index) {
        checkIndex(index);
        changeCurrentFragment(index);
        onTabSelected(mItemList.get(index));
    }

    /**
     * 子类可重写此方法
     *
     * @param bottomNavItem bottomNavItem
     */
    protected void onTabSelected(BottomNavItem bottomNavItem) {

    }

    //是否拦截此次选择
    @Override
    public boolean onInterruptSelect(int index) {
        return false;
    }

    /**
     * NavBar中间那个按钮的点击事件
     * 需要通过{@code TabMiddleView}属性设置中间按钮才有用 否则没有用
     *
     * @param v v
     */
    public void onNavBarMidViewClick(View v) {
    }


    @Override
    protected void onResume() {
        //@CallSuper 注解
        super.onResume();

        //暂时不能这样 因为上面添加fragment的顺序是不定了，等需要的时候再根据情况改吧
        //        List<Fragment> childFragments = getSupportFragmentManager().getFragments();
        //        try {
        //            childFragments.get(currentIndex).onResume();
        //        } catch (IndexOutOfBoundsException e) {
        //            LogUtil.d("数组下标越界 index = " + currentIndex + "childFragments.size = " + childFragments.size());
        //        }
    }


    //获取Title
    private String[] getTitles() {
        String[] titles = new String[mItemList.size()];
        for (int i = 0; i < mItemList.size(); i++) {
            titles[i] = mItemList.get(i).title;
        }
        return titles;
    }

    //未选中时的图标组
    private int[] getNormalIcons() {
        int[] icons = new int[mItemList.size()];
        for (int i = 0; i < mItemList.size(); i++) {
            icons[i] = mItemList.get(i).normalDrawable;
        }
        return icons;
    }

    //选中时的图标组
    private int[] getSelectedIcons() {
        int[] icons = new int[mItemList.size()];
        for (int i = 0; i < mItemList.size(); i++) {
            icons[i] = mItemList.get(i).activeDrawable;
        }
        return icons;
    }

    /**
     * 检测下标是否越界
     */
    private void checkIndex(int index) throws RuntimeException {
        if (index < 0 || index >= mItemList.size()) {
            //item元素没有那么多
            throw new RuntimeException("defaultSelectedPosition index out of the ItemList");
        }
    }

    /**
     * 对应一个item
     */
    public class BottomNavItem {

        //标题
        String title;

        //未选中时的图片
        @DrawableRes
        int normalDrawable;

        //选中时的图片
        @DrawableRes
        int activeDrawable;

        //所对应的fragment的名字
        String fragmentName;

        /**
         * 自定义底部item对象
         *
         * @param title          标题
         * @param normalDrawable 未选中状态下的图片资源
         * @param activeDrawable 选中状态下的图片资源
         * @param fragmentCLass  所对应的fragment的class  eg：{@code TestFragment.class}
         */
        public <T> BottomNavItem(String title, int normalDrawable, int activeDrawable, Class<T> fragmentCLass) {
            this.title = title;
            this.normalDrawable = normalDrawable;
            this.activeDrawable = activeDrawable;
            this.fragmentName = fragmentCLass.getName();
        }
    }

    /**
     * 不用管，没有用到
     * 从{@link BaseFragmentActivity}继承过来的
     */
    @Override
    protected final BaseFragment buildFragment() {
        return null;
    }

}
