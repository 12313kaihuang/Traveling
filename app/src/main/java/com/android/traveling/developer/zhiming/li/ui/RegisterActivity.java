package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.traveling.R;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.UtilTools;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：RegisterActivity
 * 创建者：HY
 * 创建时间：2018/10/4 16:31
 * 描述：  注册
 */

public class RegisterActivity extends BackableActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private EditText password2;
    private EditText email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

    }

    private void initView() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        email = findViewById(R.id.email);
        Button btn_register = findViewById(R.id.btn_register);
        CheckBox register_checkBox = findViewById(R.id.register_checkBox);
        TextView register_service_terms = findViewById(R.id.register_service_terms);

        btn_register.setOnClickListener(this);
        register_service_terms.setOnClickListener(this);
        register_checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btn_register.setBackgroundResource(R.drawable.blue_bg);
                btn_register.setEnabled(true);
            }else {
                btn_register.setBackgroundResource(R.drawable.btn_gray_bg);
                btn_register.setEnabled(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                //注册
                if (isInputValid()) {
                    AVUser user = new AVUser();// 新建 AVUser 对象实例
                    user.setUsername(username.getText().toString());// 设置用户名
                    user.setPassword(password.getText().toString());// 设置密码
                    user.setEmail(email.getText().toString());//设置邮箱
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                                UtilTools.toast(RegisterActivity.this, "注册成功,请进行邮箱验证后再进行登录");
                                finish();
                            } else {
                                // 失败的原因可能有多种，常见的是用户名已经存在。
                                //                        showProgress(false);
                                Toast.makeText(RegisterActivity.this,
                                        "e.code:" + e.getCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.register_service_terms:
                startActivity(new Intent(this, ServiceTermsActivity.class));
                break;
        }
    }


    //判断输入是否正确
    private boolean isInputValid() {
        if (TextUtils.isEmpty(username.getText().toString())) {
            UtilTools.toast(this, "用户名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            UtilTools.toast(this, "密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(password2.getText().toString())) {
            UtilTools.toast(this, "请再次输入密码");
            return false;
        }
        if (password.getText().toString().equals(password.getText().toString())) {
            UtilTools.toast(this, "两次输入的密码不一致！");
            return false;
        }
        if (TextUtils.isEmpty(email.getText().toString())) {
            UtilTools.toast(this, "邮箱不能为空");
            return false;
        }
        return true;
    }

}
