package com.android.traveling.developer.zhiming.li.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.traveling.R;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.entity.user.UserCallback;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.PublishDialog;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：SetPasswordActivity
 * 创建者：HY
 * 创建时间：2019/4/6 15:34
 * 描述：  设置/修改密码
 */

public class SetPasswordActivity extends BackableActivity {


    private EditText y_password;  //原密码
    private EditText x_password;  //新密码
    private Button setBtn;
    private PublishDialog dialog = new PublishDialog(this, "请稍后..");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        initView();
    }

    private void initView() {
        LinearLayout ll_y_pass = findViewById(R.id.ll_y_pass);
        x_password = findViewById(R.id.x_password);
        y_password = findViewById(R.id.y_password);
        setBtn = findViewById(R.id.btn_set);

        //添加事件
        addEvents();

        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser == null) {
            UtilTools.toast(this, "未登录！");
            return;
        }
        if (currentUser.hasPassword()) {
            setTitle("修改密码");
            ll_y_pass.setVisibility(View.GONE);
        } else {
            setTitle("设置密码");
            ll_y_pass.setVisibility(View.VISIBLE);
        }
    }

    //添加事件
    private void addEvents() {
        setBtn.setOnClickListener(v -> {
            User currentUser = TravelingUser.getCurrentUser();
            if (currentUser != null) {
                dialog.show();
                if (currentUser.hasPassword()) {
                    currentUser.changePassword(y_password.getText().toString(),
                            x_password.getText().toString(), userCallback);
                } else {
                    currentUser.changePassword(x_password.getText().toString(), userCallback);
                }
                dialog.dismiss();
            }
        });
    }

    //更改/设置密码回调接口
    private UserCallback userCallback = new UserCallback() {
        @Override
        public void onSuccess(User user) {
            UtilTools.toast(SetPasswordActivity.this, "操作成功！");
            finish();
        }

        @Override
        public void onFiled(String info) {
            UtilTools.toast(SetPasswordActivity.this, info);
        }
    };
}
