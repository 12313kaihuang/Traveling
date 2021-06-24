package com.yu.hu.traveling;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;

import com.blankj.utilcode.util.BarUtils;
import com.yu.hu.common.activity.BaseNavigationActivity;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.utils.NavOptionsBuilder;
import com.yu.hu.traveling.databinding.ActivityMainBinding;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.utils.AppConfig;
import com.yu.hu.traveling.utils.NavGraphBuilder;
import com.yu.hu.traveling.utils.UserManager;

import cn.leancloud.chatkit.utils.ChatKitUtils;

public class MainActivity extends BaseNavigationActivity<ActivityMainBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getNavHostFragmentId() {
        return R.id.nav_host_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginChatKit();

        Fragment fragment = getSupportFragmentManager().findFragmentById(getNavHostFragmentId());
        //noinspection ConstantConditions
        NavGraphBuilder.buildMainNavGraph(fragment, mNavController);
    }

    private void loginChatKit() {
        User user = UserManager.get().getUser();
        if (user != null) {
            ChatKitUtils.login(String.valueOf(user.id), new ChatKitUtils.LoginCallback() {
                @Override
                public void onLoginSuccess() {

                }

                @Override
                public void onLoginFailure() {

                }
            });
        }
    }

    /**
     * bugfix:
     * 当MainActivity因为内存不足或系统原因 被回收时 会执行该方法。
     * <p>
     * 此时会触发 FragmentManangerImpl#saveAllState的方法把所有已添加的fragment基本信息给存储起来(view没有存储)，以便于在恢复重建时能够自动创建fragment
     * <p>
     * 但是在fragment嵌套fragment的情况下，被内嵌的fragment在被恢复时，生命周期被重新调度，出现了错误。没有重新走onCreateView 方法
     * 从而会触发throw new IllegalStateException("Fragment " + fname did not create a view.");的异常
     * <p>
     * 但是在没有内嵌fragment的情况，没有问题、
     * <p>
     * <p>
     * 那我们为了解决这个问题，网络上也有许多方案，但都不尽善尽美。
     * <p>
     * 此时我们复写onSaveInstanceState，不让 FragmentManangerImpl 保存fragment的基本数据，恢复重建时，再重新创建即可
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    public void navigate(String pageUrl) {
        navigate(AppConfig.getDestItemId(pageUrl), null, NavOptionsBuilder.defaultSlideAnim());
    }

    public void navigate(String pageUrl, Bundle args) {
        navigate(AppConfig.getDestItemId(pageUrl), args, NavOptionsBuilder.defaultSlideAnim());
    }

    public void navigate(String pageUrl, NavOptions anim) {
        navigate(AppConfig.getDestItemId(pageUrl), null, anim);
    }

    public void navigate(String pageUrl, Bundle args, NavOptions anim) {
        navigate(AppConfig.getDestItemId(pageUrl), args, anim);
    }

    private void navigate(int destItemId, Bundle args, NavOptions anim) {
        mNavController.navigate(destItemId, args, anim);
    }

    /**
     * 清除掉启动页设置的全屏选项
     */
    public void clearFullScreenTheme() {
        setTheme(R.style.AppTheme);

        //更改背景为白色
        mDataBinding.getRoot().setBackgroundColor(getResources().getColor(R.color.white));

        Window window = getWindow();
        //取消全屏
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //设置白底黑字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BarUtils.setStatusBarLightMode(this, true);
        } else {
            //todo 6.0以下  未测试
            LogUtil.d("Android 6.0 以下机型");
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.theme_color));
        }
    }
}
