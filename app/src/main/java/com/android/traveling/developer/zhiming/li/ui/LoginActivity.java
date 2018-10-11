package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.traveling.MainActivity;
import com.android.traveling.R;
import com.android.traveling.util.UtilTools;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：LoginActivity
 * 创建者：HY
 * 创建时间：2018/10/4 8:30
 * 描述：  登录
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;

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
        UtilTools.setDefaultFontType(this, tv_login);

        TextView login_register = findViewById(R.id.login_register);
        login_register.setOnClickListener(this);

        //忘记密码
        TextView login_forget_pass = findViewById(R.id.login_forget_pass);
        login_forget_pass.setOnClickListener(this);

        username = findViewById(R.id.login_login);
        password = findViewById(R.id.login_password);

        //其他方式登录
        ImageView ic_wechat = findViewById(R.id.ic_wechat);
        ImageView ic_qq = findViewById(R.id.ic_qq);
        ImageView ic_weibo = findViewById(R.id.ic_weibo);
        ic_wechat.setOnClickListener(this);
        ic_qq.setOnClickListener(this);
        ic_weibo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                if (!isInputEmpty()) {
                    AVUser.logInInBackground(username.getText().toString(),
                            password.getText().toString(), new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    if (e == null) {
                                        UtilTools.toast(LoginActivity.this, "登录成功");
                                        LoginActivity.this.finish();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    } else {
                                        //                                showProgress(false);
                                        Toast.makeText(LoginActivity.this,
                                                "e.code:" + e.getCode() + e.getMessage(), Toast.LENGTH_SHORT).show();

                                        switch (e.getCode()) {
                                            case 211:
                                                //找不到用户
                                                UtilTools.toast(LoginActivity.this,
                                                        "未找到该用户，请核对您输入的用户名");
                                                break;
                                            case 216:
                                                //邮箱未被验证
                                                UtilTools.toast(LoginActivity.this,
                                                        "该用户邮箱尚未通过验证，请查看邮件并验证成功后再次登录");
                                                break;
                                        }
                                    }
                                }
                            });
                }
                break;
            case R.id.login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.service_terms:
                startActivity(new Intent(this, ServiceTermsActivity.class));
                break;
            case R.id.login_forget_pass:
                startActivity(new Intent(this, ForgetPassActivity.class));
                break;
            case R.id.ic_wechat:
                UtilTools.toast(this, "微信登录");
                break;
            case R.id.ic_qq:
                UtilTools.toast(this, "qq登录");
                break;
            case R.id.ic_weibo:
                UtilTools.toast(this, "微博登录");
                break;
        }
    }

    //判断输入是否为空
    private boolean isInputEmpty() {
        if (TextUtils.isEmpty(username.getText().toString())) {
            UtilTools.toast(this, "用户名不能为空！");
            return true;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            UtilTools.toast(this, "密码不能为空");
            return true;
        }
        return false;
    }


}
