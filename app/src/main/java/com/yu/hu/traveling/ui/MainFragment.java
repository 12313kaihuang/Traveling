package com.yu.hu.traveling.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yu.hu.common.cache.CacheManager;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.utils.NavOptionsBuilder;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentMainBinding;
import com.yu.hu.traveling.fragment.BackInterceptFragment;
import com.yu.hu.traveling.model.Destination;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.utils.AppConfig;
import com.yu.hu.traveling.utils.NavGraphBuilder;
import com.yu.hu.traveling.utils.Statics;
import com.yu.hu.traveling.utils.UserManager;
import com.yu.hu.traveling.view.AppBottomBar;
import com.yu.hu.common.fragment.WindowInsetsNavHostFragment;

import java.util.HashMap;
import java.util.Map;

import com.yu.hu.libnavannotation.FragmentDestination;

/**
 * @author Hy
 * created on 2020/04/13 17:42
 * <p>
 * 主页
 * <p>
 * 要点：
 * 1. 自定义注解 {@link FragmentDestination}
 * 2. 自定义BottomNavigationView {@link AppBottomBar}
 * 3. 根据自定义的Destination构建NavGraph {@link NavGraphBuilder#buildTabGraph(Fragment, NavController, int)}
 * 其中，构建FragmentNavigator时需要注意：
 * 如果当前NavHostFragment是在Activity中 那么需要传递的context为Activity本身，FragmentManager应为activiy.getSupporFragmentManager()
 * 如果当前NavHostFragment是在Fragment中 那么需要传递的context为requaireContext，FragmentManager应为activiy.getChildFragmentManager()
 * 4. WindowInsets下发拦截问题 {@link WindowInsetsNavHostFragment}
 * 5. 添加的观察者被隐藏后需要全部清除掉 否则会被调用到。。
 **/
@FragmentDestination(pageUrl = "main")
public class MainFragment extends BackInterceptFragment<FragmentMainBinding>
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NavController mNavController;
    private AppBottomBar mNavigationView;

    private MutableLiveData<User> loginLivaData;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        ((MainActivity) mActivity).clearFullScreenTheme();

        mNavigationView = mRootView.findViewById(R.id.bottom_bar);
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.nav_main_fragment_container);
        //noinspection ConstantConditions
        mNavController = NavHostFragment.findNavController(fragment);
        NavGraphBuilder.buildTabGraph(fragment, mNavController);
        //权限申请
        CacheManager.requestPermissionsOnlyOnce(Statics.CACHE_KEY_REQUEST_PERMISSIONS, Statics.PERMISSIONS_ALL_NEED);
        //底部导航点击事件监听
        mNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Map.Entry<String, Destination> entry : destConfig.entrySet()) {
            Destination value = entry.getValue();
            if (value != null && value.id == item.getItemId()) {
                //登录拦截
                if (value.needLogin && !UserManager.get().isLogin()) {
                    getLoginLivaData().observe(this, user -> {
                        if (user == null) {
                            LogUtil.d("user == null");
                            loginLivaData.removeObservers(this);
                            return;
                        }
                        LogUtil.d("登录完成 - itemId = " + item.getItemId());
                        if (value.tabPage) {
                            mNavigationView.setSelectedItemId(item.getItemId());
                        } else {
                            ((MainActivity) mActivity).getNavController().navigate(item.getItemId(), null, NavOptionsBuilder.defaultSlideAnim());
                        }
                    });
                    return false;
                } else if (!value.tabPage) {
                    ((MainActivity) mActivity).getNavController().navigate(item.getItemId(), null, NavOptionsBuilder.defaultSlideAnim());
                    return false;
                }
            }
        }

        mNavController.navigate(item.getItemId());
        //返回true代表这个item被选中了，即会被着色，反之不会被着色
        return !TextUtils.isEmpty(item.getTitle());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && loginLivaData != null) {
            loginLivaData.removeObservers(this);
            loginLivaData = null;
        }
    }

    @Override
    public void handleOnBackPressed() {
        if (!isVisible()) {
            return;
        }
        mActivity.finish();
    }

    private MutableLiveData<User> getLoginLivaData() {
        if (loginLivaData == null) {
            loginLivaData = UserManager.get().login(mActivity);
        }
        return loginLivaData;
    }
}
