package com.android.traveling.developer.zhiming.li.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.traveling.R;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.entity.user.UserCallback;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;

import java.util.regex.Pattern;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：BindEmailActivity
 * 创建者：HY
 * 创建时间：2018/10/17 16:59
 * 描述：  绑定邮箱
 */

public class BindEmailActivity extends BackableActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private LinearLayout ll_pass;

    private boolean needPass = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button btn_bind_email = findViewById(R.id.btn_bind_email);
        ll_pass = findViewById(R.id.ll_pass);

        btn_bind_email.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        User user = TravelingUser.getCurrentUser();
        if (user == null) {
            return;
        }
        if (user.hasPassword()) {
            ll_pass.setVisibility(View.GONE);
            needPass = false;
        } else {
            ll_pass.setVisibility(View.VISIBLE);
            needPass = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind_email:
                if (inputValid()) {
                    User currentUser = TravelingUser.getCurrentUser();
                    if (currentUser == null) {
                        break;
                    }
                    currentUser.setEmail(email.getText().toString());
                    String pass = null;
                    if (needPass) {
                        pass = password.getText().toString();
                        currentUser.setPassword(password.getText().toString());
                    }
                    currentUser.bindEmail(pass, new UserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            UtilTools.toast(BindEmailActivity.this,
                                    "邮件已发送，点击链接验证后即可成功绑定，请及时查收", Toast.LENGTH_SHORT);
                            finish();
                        }

                        @Override
                        public void onFiled(String info) {
                            UtilTools.toast(BindEmailActivity.this, info);
                            finish();
                        }
                    });
                }
                break;
        }
    }

    /**
     * email输入是否合法
     *
     * @return 判断email输入是否合法
     */
    private boolean inputValid() {

        if (TextUtils.isEmpty(email.getText().toString())) {
            UtilTools.toast(this, "请输入所需绑定的邮箱");
            return false;
        }

        if (!Pattern.matches(StaticClass.EMAIL_REGULAR, email.getText().toString())) {
            UtilTools.toast(this, "请输入正确的邮箱");
            return false;
        }

        if (needPass) {
            if (TextUtils.isEmpty(password.getText().toString()) ||
                    password.getText().toString().length() < 5) {
                UtilTools.toast(this, "请输入5-20位的密码");
                return false;
            }
        }

        return true;
    }
}
