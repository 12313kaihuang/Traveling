package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.traveling.MainActivity;
import com.android.traveling.R;
import com.android.traveling.util.UtilTools;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：LoginActivity
 * 创建者：HY
 * 创建时间：2018/10/4 8:30
 * 描述：  登录
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    //初始化View
    private void initView() {
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        TextView service_terms = findViewById(R.id.service_terms);
        service_terms.setOnClickListener(this);

        //设置字体
        TextView tv_login = findViewById(R.id.tv_login);
        UtilTools.setDefaultFontType(this,tv_login);

        TextView login_register = findViewById(R.id.login_register);
        login_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                UtilTools.toast(this,"登录成功");
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.login_register:

                break;
            case R.id.service_terms:
                startActivity(new Intent(this, ServiceTermsActivity.class));
                break;
        }
    }
}
