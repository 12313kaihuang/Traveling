package com.android.traveling.developer.yu.hu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.widget.ImageView;

import com.android.traveling.developer.yu.hu.fragment.FocusOnFragment;
import com.android.traveling.developer.yu.hu.fragment.NewFragment;
import com.android.traveling.developer.yu.hu.fragment.RecommendFragment;
import com.android.traveling.R;
import com.android.traveling.fragment.BaseFragment;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu
 * 文件名：HomeFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    ViewPager viewPager;
    //Title
    private List<String> titles;
    //Fragment
    private List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initData();
        initView(view);
        return view;

    }

    //初始化数据
    private void initData() {
        titles = new ArrayList<>();
        titles.add(getString(R.string.fragment_new));
        titles.add(getString(R.string.fragment_recommend));
        titles.add(getString(R.string.fragment_focus_on));

        fragments = new ArrayList<>();
        fragments.add(new NewFragment());
        fragments.add(new RecommendFragment());
        fragments.add(new FocusOnFragment());
    }

    //初始化view
    private void initView(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        //搜索图标
        ImageView home_search = view.findViewById(R.id.home_search);
        home_search.setOnClickListener(this);

        //预加载  todo
        viewPager.setOffscreenPageLimit(2);

        //设置适配器
        //这里要使用getChildFragmentManager 否则HomeFragment再次创建时，子Fragment不会显示
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            //设置标题
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });

        //默认显示"推荐"
        viewPager.setCurrentItem(1);

        //绑定
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search:
                UtilTools.toast(getContext(),"点击了搜索图标");
                break;
            default:
                 break;
        }
    }
}
