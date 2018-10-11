package com.android.traveling.developer.zhiming.li.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.traveling.R;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.UtilTools;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：ForgetPassActivity
 * 创建者：HY
 * 创建时间：2018/10/11 20:59
 * 描述：  忘记密码
 */

public class ForgetPassActivity extends BackableActivity implements View.OnClickListener {

    private EditText forget_pass_email;
//    private EditText forget_pass_pass;
//    private EditText forget_pass_pass2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        initView();
    }

    private void initView() {
        forget_pass_email = findViewById(R.id.forget_pass_email);
//        forget_pass_pass = findViewById(R.id.forget_pass_pass);
//        forget_pass_pass2 = findViewById(R.id.forget_pass_pass2);

        Button btn_find_pass = findViewById(R.id.btn_find_pass);
        btn_find_pass.setOnClickListener(this);
    }

    private boolean isInputValid() {

        if (TextUtils.isEmpty(forget_pass_email.getText().toString())) {
            UtilTools.toast(this, getString(R.string.hint_email));
            return false;
        }
//        if (TextUtils.isEmpty(forget_pass_pass.getText().toString())) {
//            UtilTools.toast(this, getString(R.string.new_pass));
//            return false;
//        }
//        if (TextUtils.isEmpty(forget_pass_pass2.getText().toString())) {
//            UtilTools.toast(this, getString(R.string.repeat_pass));
//            return false;
//        }
//        if (forget_pass_pass.getText().toString().equals(forget_pass_pass2.getText().toString())) {
//            UtilTools.toast(this, "两次输入的密码不一致！");
//            return false;
//        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find_pass:
                if (isInputValid()) {
                    AVUser.requestPasswordResetInBackground(forget_pass_email.getText().toString(),
                            new RequestPasswordResetCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                UtilTools.toast(ForgetPassActivity.this, "请前往邮箱进行密码重置");
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }
    }
}
