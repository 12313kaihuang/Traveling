package com.yu.hu.traveling.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentMain2Binding;
import com.yu.hu.traveling.fragment.BackInterceptFragment;
import com.yu.hu.traveling.model.Destination;
import com.yu.hu.traveling.utils.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hy
 * created on 2020/04/18 19:49
 * <p>
 * 通过ViewPager2构建导航
 **/
public class MainFragment2 extends BackInterceptFragment<FragmentMain2Binding> implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main2;
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity) mActivity).clearFullScreenTheme();

        mDataBinding.bottomBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Map.Entry<String, Destination> entry : destConfig.entrySet()) {
            Destination value = entry.getValue();
            if (value != null && value.id == item.getItemId()) {
                if (!value.tabPage) {
                    //发布页面
                    Navigation.findNavController(mActivity, R.id.nav_host_fragment)
                    .navigate(R.id.action_mainFragment2_to_publishFragment);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void handleOnBackPressed() {
        LogUtil.i("退出");
        mActivity.finish();
    }
}
