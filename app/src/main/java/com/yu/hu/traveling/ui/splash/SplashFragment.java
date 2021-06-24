package com.yu.hu.traveling.ui.splash;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentSplashBinding;

import com.yu.hu.traveling.fragment.BackInterceptFragment;
import com.yu.hu.traveling.ui.MainFragment;
import com.yu.hu.traveling.utils.AppConfig;

/**
 * @author Hy
 * created on 2020/04/13 16:56
 * <p>
 * 启动/闪屏页
 * <p>
 * 要点：
 * 1. 启动优化（屏蔽back键等）、异形屏适配 https://www.jianshu.com/p/251d1c5dc0db
 * 2. 跳转至主页后主题样式切换  {@link MainActivity#clearFullScreenTheme()}
 * 3. 跳转至主页后销毁此页面 {@link MainFragment#onInitEvents(View, Bundle)}
 * 4. 跳转至主页后按back键退出app
 **/
@FragmentDestination(pageUrl = "splash", asStarter = true)
public class SplashFragment extends BackInterceptFragment<FragmentSplashBinding> {

    private NavController mNavController;
    private SplashViewModel mViewModel;

    //剩余时间的监听
    private Observer<Long> resetTimeObserver = resetTime -> {
        //qq授权登录回来之后会再次触发此事件
        if (!isVisible()) {
            return;
        }
        if (resetTime == 0) {
            toMainPage();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        mNavController = Navigation.findNavController(mActivity, R.id.nav_host_fragment);
        mViewModel = new ViewModelProvider(mActivity).get(SplashViewModel.class);
        mDataBinding.setViewModel(mViewModel);

        mViewModel.getRestTime().observe(this, resetTimeObserver);
    }

    //跳转至主页
    private void toMainPage() {
        mNavController.navigate(AppConfig.getDestItemId("main"));
        //mNavController.navigate(R.id.splash_to_Main2);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mDataBinding.tvSkip.setOnClickListener(v -> {
            //移除监听 否则剩余1s时点击跳过会出现两次跳转至MainActivity情况
            mViewModel.getRestTime().removeObserver(resetTimeObserver);
            toMainPage();
        });

        //开始倒计时
        mViewModel.startInterval();
        //加载图片
        mViewModel.loadImage(this);
        //获取底部导航栏的配置信息
        mViewModel.getBottomBarConfig();
        //获取首页（homeFragment）tab配置信息
        mViewModel.getHomeTabConfig();
    }

    @Override
    public void handleOnBackPressed() {
        LogUtil.i("拦截Back事件");
        if (!isVisible()) {
            Log.e("SplashFragment", "已隐藏但仍拦截了back事件");
            ((MainActivity) mActivity).popBackStack();
        }
    }
}
