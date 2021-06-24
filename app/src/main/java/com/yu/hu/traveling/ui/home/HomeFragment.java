package com.yu.hu.traveling.ui.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.common.utils.NavOptionsBuilder;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.databinding.FragmentHomeBinding;
import com.yu.hu.traveling.ui.search.SearchFragment;

/**
 * @author Hy
 * created on 2020/04/14 23:16
 * <p>
 * 首页
 **/
@FragmentDestination(pageUrl = "main/tabs/home", tabPage = true, asStarter = true, iconRes = R.drawable.icon_tab_home)
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private HomeViewModel mViewModel;
    private TabLayoutMediator mediator;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {

        mViewModel = createViewModel(HomeViewModel.class);

        //禁用预加载效果
        mDataBinding.viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        //viewPager2默认只有一种类型的Adapter。FragmentStateAdapter
        //并且在页面切换的时候 不会调用子Fragment的setUserVisibleHint ，取而代之的是onPause(),onResume()、
        mDataBinding.viewPager.setAdapter(mViewModel.createAdapter(getChildFragmentManager(), getLifecycle()));
        //mDataBinding.tabLayout.setTabGravity(mViewModel.tabConfig.tabGravity);
        //autoRefresh 自动刷新  由于这里配置信息拉取到之后就不需要更改UI了  所以这里填的false
        mediator = new TabLayoutMediator(mDataBinding.tabLayout, mDataBinding.viewPager,
                false, mViewModel.createTabConfigurationStrategy());
        //开启viewPager与TabLayout的联动
        mediator.attach();
        //选中监听
        mDataBinding.viewPager.registerOnPageChangeCallback(mOnPageChangeCallback);
        //设置选中页  延迟一下  页面渲染完成之后set才有用
        //如果需要设置默认起始页需要要带上smoothScroll这个参数并设为false  否则禁用预加载将失效
        mDataBinding.viewPager.post(() -> mDataBinding.viewPager.setCurrentItem(mViewModel.tabConfig.select, false));

        //跳转搜索页面
        mDataBinding.searchBtn.setOnClickListener(v -> ((MainActivity) mActivity).navigate(SearchFragment.PAGE_URL, NavOptionsBuilder.defaultScaleAnim()));
    }

    @Override
    public void onDestroy() {
        mediator.detach();
        mDataBinding.viewPager.unregisterOnPageChangeCallback(mOnPageChangeCallback);
        super.onDestroy();
    }

    private ViewPager2.OnPageChangeCallback mOnPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @SuppressWarnings("ConstantConditions")
        @Override
        public void onPageSelected(int position) {
            int tabCount = mDataBinding.tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = mDataBinding.tabLayout.getTabAt(i);
                TextView tabView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {
                    tabView.setTextSize(mViewModel.tabConfig.activeSize);
                    tabView.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    tabView.setTextSize(mViewModel.tabConfig.normalSize);
                    tabView.setTypeface(Typeface.DEFAULT);
                }
            }
        }
    };

}
