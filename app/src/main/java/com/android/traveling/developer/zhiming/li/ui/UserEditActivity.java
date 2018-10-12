package com.android.traveling.developer.zhiming.li.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：UserEditActivity
 * 创建者：HY
 * 创建时间：2018/10/12 19:50
 * 描述：  编辑个人资料
 */

public class UserEditActivity extends AppCompatActivity implements View.OnClickListener {

    private AVUser user;
    private EditText username;
    private EditText gender;
    private EditText live_area;
    private EditText signature;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        initView();
        initData();
    }

    //初始化数据
    private void initData() {

        //同步对象
        user.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                username.setText(user.getUsername());

                String s_signature = user.getString("signature");
                if (s_signature != null) {
                    signature.setText(s_signature);
                }
                String s_live_area = user.getString("live_area");
                if (s_live_area != null) {
                    live_area.setText(s_live_area);
                }
                String s_gender = user.getString("gender");
                if (s_gender != null) {
                    gender.setText(s_gender);
                }
            }
        });

    }

    //初始化View
    private void initView() {
        user = AVUser.getCurrentUser();
        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        live_area = findViewById(R.id.live_area);
        signature = findViewById(R.id.signature);
        ImageView img_back = findViewById(R.id.img_back);
        TextView save = findViewById(R.id.save);

        save.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                user.put("username", username.getText().toString());
                user.put("signature", signature.getText().toString());
                user.put("gender", gender.getText().toString());
                user.put("live_area", live_area.getText().toString());
                user.saveInBackground();
                UtilTools.toast(this, "保存成功");
                onBackPressed();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("UserEditActivity Destroy");
    }
}
