package com.yu.hu.traveling.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.library.activity.BaseNavigationActivity;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.fragment.ChatListFragment;
import com.yu.hu.traveling.fragment.HomeFragment;
import com.yu.hu.traveling.fragment.MyTestFragment;
import com.yu.hu.traveling.fragment.MyFragment;
import com.yuntongxun.plugin.common.common.utils.EasyPermissionsEx;
import com.yuntongxun.plugin.common.ui.RongXinFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.activity
 * 文件名：MainActivity
 * 创建者：HY
 * 创建时间：2019/6/15 16:33
 * 描述：  MainActivity
 */
public class MainActivity extends BaseNavigationActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        //申请权限
        initPermission();
    }

    @Override
    public void onNavBarMidViewClick(View v) {
        ToastUtils.showShort("发表游记");
    }

    @Override
    public List<BottomNavItem> getItemList() {
        List<BottomNavItem> itemList = new ArrayList<>();

        //首页
        itemList.add(new BottomNavItem(getString(R.string.page_home),
                R.drawable.ic_home_normal,
                R.drawable.ic_home_selected,
                HomeFragment.class));

        //结伴
        itemList.add(new BottomNavItem(getString(R.string.page_friend),
                R.drawable.ic_friends_normal,
                R.drawable.ic_friends_selected,
                MyTestFragment.class));

        //消息
        itemList.add(new BottomNavItem(getString(R.string.page_message),
                R.drawable.ic_message_normal,
                R.drawable.ic_message_selected,
                ChatListFragment.class));

        //我的
        itemList.add(new BottomNavItem(getString(R.string.page_my),
                R.drawable.ic_my_normal,
                R.drawable.ic_my_selected,
                MyFragment.class));

        return itemList;
    }

    @Override
    public int getNormalColor() {
        return R.color.color_normal;
    }

    @Override
    public int getActiveColor() {
        return R.color.color_selected;
    }

    /**
     * 申请权限
     */
    private void initPermission() {
        String rationInit = "需要存储、相机和麦克风的权限";
        if (!EasyPermissionsEx.hasPermissions(MainActivity.this, RongXinFragmentActivity.needPermissionsCamera)) {
            EasyPermissionsEx.requestPermissions(MainActivity.this, rationInit, RongXinFragmentActivity.PERMISSIONS_REQUEST_CAMERA, RongXinFragmentActivity.needPermissionsCamera);
            LogUtil.d("拒绝了相机权限");
        }

        //录音权限申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            }
        }
    }
}
