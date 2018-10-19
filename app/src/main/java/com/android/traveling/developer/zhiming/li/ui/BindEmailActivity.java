package com.android.traveling.developer.zhiming.li.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.traveling.R;
import com.android.traveling.entity.MyUser;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;

import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

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

    private void initData() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (user.isHasPass()) {
            ll_pass.setVisibility(View.GONE);
            needPass = false;
        } else {
            ll_pass.setVisibility(View.VISIBLE);
            needPass = true;
        }
    }

    private void initView() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button btn_bind_email = findViewById(R.id.btn_bind_email);
        ll_pass = findViewById(R.id.ll_pass);

        btn_bind_email.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind_email:
                if (inputValid()) {
                    MyUser user = BmobUser.getCurrentUser(MyUser.class);
                    user.setUsername(email.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setEmailVerified(false);
                    if (needPass) {
                        user.setPassword(password.getText().toString());
                    }
                    BmobUser.requestEmailVerify(email.getText().toString(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                user.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        UtilTools.toast(BindEmailActivity.this,
                                                "绑定成功，请到" + email.getText().toString() + "邮箱中进行激活。");
                                        finish();
                                    }
                                });
                            }else{
                                UtilTools.toastException(BindEmailActivity.this,e);
                            }
                        }
                    });
                }
                break;
        }
    }

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
