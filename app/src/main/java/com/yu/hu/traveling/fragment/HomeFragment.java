package com.yu.hu.traveling.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.android.material.tabs.TabLayout;
import com.yu.hu.library.fragment.BaseFragment;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.activity.SearchResultActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.fragment
 * 文件名：HomeFragment
 * 创建者：HY
 * 创建时间：2019/7/1 14:37
 * 描述：  首页
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    //Title
    private List<String> titles;
    //Fragment
    private List<Fragment> fragments;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        initView(mView);


    }

    /**
     * 跳转到搜索游记页面
     */
    @OnClick({R.id.search_view, R.id.ib_voice})
    void toSearchNote(View v) {
        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
        switch (v.getId()) {
            case R.id.ib_voice:
                //开启语音
                intent.putExtra(SearchResultActivity.IS_NEED_VOICE, "need");
                break;
        }
        ActivityUtils.startActivity(intent);
    }

    //初始化view
    private void initView(View view) {
        initData();
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

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

    //初始化数据
    private void initData() {
        titles = new ArrayList<>();
        titles.add(getString(R.string.fragment_new));
        titles.add(getString(R.string.fragment_recommend));
        titles.add(getString(R.string.fragment_focus_on));

        fragments = new ArrayList<>();
        fragments.add(new MyTestFragment());
        fragments.add(new MyTestFragment());
        fragments.add(new MyTestFragment());
        LogUtil.d("initData  size=" + fragments.size());
    }


}
