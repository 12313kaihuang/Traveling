package com.yu.hu.traveling.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.yu.hu.library.activity.BaseSplashActivity;
import com.yu.hu.library.util.DateUtil;
import com.yu.hu.library.util.SharedPreferencesUtil;
import com.yu.hu.library.util.Utils;
import com.yu.hu.traveling.entity.Const;
import com.yu.hu.traveling.R;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.activity
 * 文件名：SplashActivity
 * 创建者：HY
 * 创建时间：2019/6/15 16:33
 * 描述：  SplashActivity
 */
public class SplashActivity extends BaseSplashActivity {

    @BindView(R.id.tv_splash)
    TextView mSplashTv;

    @BindView(R.id.tv_copy_right)
    TextView mCopyRightTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        //设置字体
        Utils.setFont(this, mSplashTv, Const.DEFAULT_FONT_SPLASH);
        //CopyRight
        mCopyRightTv.setText(getString(R.string.copy_right_declaration, DateUtil.getYear()));
    }

    @Override
    protected void skipTo() {
        if (isFirstRunning()) {
            ActivityUtils.startActivity(GuideActivity.class);
        } else {
            ActivityUtils.startActivity(MainActivity.class);
        }
    }

    @Override
    protected long getSplashDelay() {
        return 2;
    }

    @Override
    protected TimeUnit getSplashTimeUnit() {
        return TimeUnit.SECONDS;
    }

    //判断程序是否第一次运行
    private boolean isFirstRunning() {
        Boolean isFirstRunning =
                SharedPreferencesUtil.getBoolean(Const.IS_FIRST_RUN, true);
        if (isFirstRunning) {
            //是第一次运行
            SharedPreferencesUtil.putBoolean(Const.IS_FIRST_RUN, false);
        }
        return isFirstRunning;
    }
}
