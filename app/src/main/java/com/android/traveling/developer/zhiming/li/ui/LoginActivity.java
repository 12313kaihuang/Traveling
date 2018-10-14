package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.traveling.MainActivity;
import com.android.traveling.R;
import com.android.traveling.util.StaticClass;
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
    private inputContent inputContent = new inputContent();

    //登录方式
    private int loginMode = StaticClass.LOGIN_BY_VERIFIED;
    private boolean passwordMode = true;  //true为手机账号登录 false为邮箱账号登录
    private TextView login_forget_pass;
    private TextView login_by_password;
    private TextView cut_line;
    private TextView login_by_verified;
    private LinearLayout LL_verified;

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

        //忘记密码
        login_forget_pass = findViewById(R.id.login_forget_pass);
        login_forget_pass.setOnClickListener(this);

        username = findViewById(R.id.login_login);
        password = findViewById(R.id.login_password);
        cut_line = findViewById(R.id.cut_line);
        LL_verified = findViewById(R.id.LL_verified);
        login_by_password = findViewById(R.id.login_by_password);
        login_by_verified = findViewById(R.id.login_by_verified);
        login_by_password.setOnClickListener(this);
        login_by_verified.setOnClickListener(this);

        addTextChangedListener();

        //其他方式登录
        ImageView ic_wechat = findViewById(R.id.ic_wechat);
        ImageView ic_qq = findViewById(R.id.ic_qq);
        ImageView ic_weibo = findViewById(R.id.ic_weibo);
        ic_wechat.setOnClickListener(this);
        ic_qq.setOnClickListener(this);
        ic_weibo.setOnClickListener(this);
    }

    //添加editText输入监听事件
    private void addTextChangedListener() {

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (!isInputEmpty()) {
                    Login();

                }
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
            case R.id.login_by_password:
                changePasswordMode();
                break;
            case R.id.login_by_verified:
                changeLoginMode();
                break;
        }
    }

    //登录
    private void Login() {
        switch (loginMode) {
            case StaticClass.LOGIN_BY_VERIFIED:

                break;
            case StaticClass.LOGIN_BY_PHONE:

                break;
            case StaticClass.LOGIN_BY_EMAIL:
                loginByEmail();
                break;
        }
    }

    //邮箱账号登录
    private void loginByEmail() {
        AVUser.logInInBackground(username.getText().toString(),
                password.getText().toString(), new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            UtilTools.toast(LoginActivity.this, "登录成功");
                            LoginActivity.this.finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            toastException(e);
                        }
                    }
                });
    }

    //切换登录方式
    private void changeLoginMode() {
        if (loginMode == StaticClass.LOGIN_BY_VERIFIED) {

            LL_verified.setVisibility(View.INVISIBLE);
            password.setVisibility(View.VISIBLE);
            login_by_password.setVisibility(View.VISIBLE);
            login_forget_pass.setVisibility(View.VISIBLE);
            cut_line.setVisibility(View.VISIBLE);
            login_by_verified.setText(getString(R.string.login_by_verified));
            if (passwordMode) {
                setInputType(StaticClass.LOGIN_BY_PHONE);
            } else {
                setInputType(StaticClass.LOGIN_BY_EMAIL);
                username.setHint(getString(R.string.input_email));
                username.setText(inputContent.email);
            }

        } else {
            setInputType(StaticClass.LOGIN_BY_VERIFIED);
            if (passwordMode) {
                inputContent.phoneNumber = username.getText().toString();
            } else {
                inputContent.email = username.getText().toString();
                username.setText(inputContent.phoneNumber);
            }
            LL_verified.setVisibility(View.VISIBLE);
            password.setVisibility(View.INVISIBLE);
            login_by_password.setVisibility(View.GONE);
            login_forget_pass.setVisibility(View.GONE);
            cut_line.setVisibility(View.GONE);
            username.setHint(getString(R.string.input_phone));
            login_by_verified.setText(getString(R.string.login_by_password));
        }
    }

    //切换登录方式
    private void changePasswordMode() {
        switch (loginMode) {
            case StaticClass.LOGIN_BY_EMAIL:
                setInputType(StaticClass.LOGIN_BY_PHONE);
                passwordMode = true;
                login_by_password.setText(getString(R.string.login_by_email));
                username.setHint(getString(R.string.input_phone));

                inputContent.email = username.getText().toString();
                username.setText(inputContent.phoneNumber);
                break;
            case StaticClass.LOGIN_BY_PHONE:
                passwordMode = false;
                setInputType(StaticClass.LOGIN_BY_EMAIL);
                login_by_password.setText(getString(R.string.login_by_phone));
                username.setHint(getString(R.string.input_email));

                inputContent.phoneNumber = username.getText().toString();
                username.setText(inputContent.email);
                break;

        }

    }

    //设置username的类型同时设置loginMode
    private void setInputType(int loginMode) {
        this.loginMode = loginMode;
        switch (loginMode) {
            case StaticClass.LOGIN_BY_VERIFIED:
            case StaticClass.LOGIN_BY_PHONE:
                username.setInputType(InputType.TYPE_CLASS_PHONE);
                username.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(11)
                });
                break;
            case StaticClass.LOGIN_BY_EMAIL:
                username.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                username.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(50)
                });
                break;
        }

    }

    //根据异常码打印出错误信息
    private void toastException(AVException e) {

        switch (e.getCode()) {
            case 211:
                //找不到用户
                UtilTools.toast(LoginActivity.this,
                        "未找到该用户，请核对您输入的用户名");
                break;
            case 215:
                //手机号未被验证
                UtilTools.toast(LoginActivity.this,
                        "该手机尚未通过验证");
                break;
            case 216:
                //邮箱未被验证
                UtilTools.toast(LoginActivity.this,
                        "该邮箱尚未通过验证，请查看邮件并验证成功后再次登录");
                break;
            default:
                Toast.makeText(LoginActivity.this,
                        "e.code:" + e.getCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    class inputContent {
        String phoneNumber = "";
        String email = "";
    }
}
