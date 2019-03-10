package com.android.traveling;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.traveling.developer.jiaming.liu.activity.AddNoteActivity;
import com.android.traveling.developer.ting.li.ui.FriendsFragment;


import com.android.traveling.developer.yu.hu.HomeFragment;
import com.android.traveling.developer.jiaming.liu.MessageFragment;
import com.android.traveling.developer.zhiming.li.MyFragment;
import com.android.traveling.developer.zhiming.li.ui.LoginActivity;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.viewpager.NoScrollViewPager;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NoScrollViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //搜索框输入时底部tabLayout不会再被弹起
//        getWindow().setSoftInputMode
//                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
//                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        initFragments();
        initView();


        User user = TravelingUser.getCurrentUser();
        if (user != null) {
            try {
                LCChatKit.getInstance().open(String.valueOf(user.getUserId()), new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (null == e) {
                            LogUtil.d("===============done: " + user.getUserId() + " 登录LeanCloud成功");
                        } else {
                            UtilTools.toast(MainActivity.this, "LeanClound登录失败");
                            LogUtil.d("===============done: " + user.getUserId() + " 登录LeanCloud失败");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d("MainActivity  LeanCloud open异常");
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.d("===================mainActivity onNewIntent=======================");
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
            case R.id.center_btn: {
                showBottomDialog();
                break;
            }
            case R.id.id_dialog_centeradd_travels:{
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("type","游记");
                startActivity(intent);
                break;
            }
            case R.id.id_dialog_centeradd_strategy:{
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("type","攻略");
                startActivity(intent);
                break;
            }
            case R.id.id_dialog_centeradd_friends: {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("type", "结伴");
                startActivity(intent);
                break;
            }
        }
    }

    private void setCurrentFragment(int position) {
        //去除滚动效果
        viewPager.setCurrentItem(position);
    }
    private void showBottomDialog() {
        final BottomSheetDialog dialog=new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_centeradd,null);
        LinearLayout travels = dialogView.findViewById(R.id.id_dialog_centeradd_travels);
        LinearLayout strategy = dialogView.findViewById(R.id.id_dialog_centeradd_strategy);
        LinearLayout friends = dialogView.findViewById(R.id.id_dialog_centeradd_friends);
        ImageButton cancel = dialogView.findViewById(R.id.id_dialog_centeradd_cancel);
        travels.setOnClickListener(this);
        strategy.setOnClickListener(this);
        friends.setOnClickListener(this);

        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
