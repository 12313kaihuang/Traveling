package com.example.test;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yu.hu.common.activity.BaseActivity;
import com.yu.hu.traveling.R;

/**
 * create by hy on 2019/11/22 23:45
 */
public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new TestFragment())
                .commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second;
    }
}
