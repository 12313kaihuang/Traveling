package com.yu.hu.library.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.yu.hu.library.R;
import com.yu.hu.library.mvp.BasePresenter;
import com.yu.hu.library.mvp.Presence;
import com.yu.hu.library.util.LogUtil;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 文件名：BackableActivity
 * 创建者：HY
 * 创建时间：2018/9/22 11:57
 * 描述：  带有返回键的Activity
 * <p>
 * 使用系统自带的ActionBar
 * 不太好封装，放在这里可参考实现过程
 */


@SuppressLint("Registered")
public abstract class BackableActivity<I extends Presence, P extends BasePresenter<I>>
        extends BaseActivity<I,P> {

    @CallSuper
    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        //设置主题
        setTheme(R.style.BackableActivity);

        //显示返回键
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            LogUtil.e("BackableActivity初始化失败");
            throw new RuntimeException("BackableActivity init failure");
        }
    }

    //菜单栏操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回键的id
            case android.R.id.home:
                onBack();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onBack();
        finish();
    }

    //按下返回键触发(上部返回键或底部返回键)
    public void onBack() {

    }
}
