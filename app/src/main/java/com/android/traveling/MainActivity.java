package com.android.traveling;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.android.traveling.developer.ting.li.ui.FriendsFragment;
import com.android.traveling.developer.yu.hu.HomeFragment;
import com.android.traveling.developer.jiaming.liu.MessageFragment;
import com.android.traveling.developer.zhiming.li.MyFragment;
import com.android.traveling.util.UtilTools;
import com.android.traveling.viewpager.NoScrollViewPager;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NoScrollViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        initView();
    }

    private void initFragments() {
        fragments.add(new HomeFragment());
        fragments.add(new FriendsFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MyFragment());
    }

    //初始化View
    private void initView() {

        JPTabBar mTabbar = findViewById(R.id.tabbar);
        viewPager = findViewById(R.id.fragment_content);
        //初始化底部tabBar
        mTabbar.setTitles(R.string.fragment_home, R.string.fragment_friends,
                R.string.fragment_message, R.string.fragment_my)
                .setNormalIcons(R.drawable.ic_home_normal, R.drawable.ic_friends_normal,
                        R.drawable.ic_message_normal, R.drawable.ic_my_normal)
                .setSelectedIcons(R.drawable.ic_home_selected, R.drawable.ic_friends_selected,
                        R.drawable.ic_message_selected, R.drawable.ic_my_selected)
                .generate();

        //获取中间的加号按钮
        View middleView = mTabbar.getMiddleView();
        CircleImageView circleImageView = middleView.findViewById(R.id.center_btn);
        circleImageView.setOnClickListener(this);

        //设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            //只加载一次
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //super.destroyItem(container, position, object);
            }
        });

        //设置默认首页
        setCurrentFragment(0);

        //添加监听事件
        mTabbar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int index) {
                setCurrentFragment(index);
            }

            @Override
            public boolean onInterruptSelect(int index) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.center_btn:
                UtilTools.toast(this, "点击了中间的按钮");
                break;
        }
    }

    private void setCurrentFragment(int position) {
        //去除滚动效果
        viewPager.setCurrentItem(position);
    }
}
