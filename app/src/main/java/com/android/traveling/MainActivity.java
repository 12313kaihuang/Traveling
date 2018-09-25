package com.android.traveling;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.traveling.fragment.FriendsFragment;
import com.android.traveling.fragment.HomeFragment;
import com.android.traveling.fragment.MessageFragment;
import com.android.traveling.fragment.MyFragment;
import com.android.traveling.util.UtilTools;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //去掉阴影
        //noinspection ConstantConditions
        getSupportActionBar().setElevation(0);

        initView();
    }

    //初始化View
    private void initView() {

        JPTabBar mTabbar = findViewById(R.id.tabbar);
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

    //动态设置当前显示哪个Fragment
    private void setCurrentFragment(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                transaction.replace(R.id.fragment_content, new HomeFragment());
                break;
            case 1:
                transaction.replace(R.id.fragment_content, new FriendsFragment());
                break;
            case 2:
                transaction.replace(R.id.fragment_content, new MessageFragment());
                break;
            case 3:
                transaction.replace(R.id.fragment_content, new MyFragment());
                break;
            default:
                break;
        }
        transaction.commit();
    }
}
