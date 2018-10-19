package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.util.MyCustomDialog;
import com.android.traveling.entity.MyUser;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：AccountBindActivity
 * 创建者：HY
 * 创建时间：2018/10/16 16:55
 * 描述：  账号绑定与设置
 */

public class AccountBindActivity extends BackableActivity implements View.OnClickListener {

    private TextView bind_uid;
    private TextView bind_phone;
    private TextView bind_email;
    private Button btn_email;

    private boolean hasBindEmail = false;
    private MyCustomDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);

        //从云端获取最新User信息
        //        fetchUserInfo();
        initView();
        initData();
    }

    private void fetchUserInfo() {

        loadingDialog = MyCustomDialog.getLoadingDialog(this);
        loadingDialog.show();
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    loadingDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        MyUser user =
                                new Gson().fromJson(jsonObject.toString(), MyUser.class);
                        MyUser currentUser = new MyUser();
                        currentUser.setHasPass(user.isHasPass());
                        currentUser.update(BmobUser.getCurrentUser(MyUser.class).getObjectId(),
                                new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            runOnUiThread(() -> {
                                                initView();
                                                initData();
                                            });
                                        } else {
                                            UtilTools.toast(AccountBindActivity.this,
                                                    "更新用户信息失败:" + e.getMessage());
                                        }
                                    }
                                });

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    UtilTools.toastException(AccountBindActivity.this, e);
                }
            }
        });
    }

    //初始化控件
    private void initView() {

        bind_uid = findViewById(R.id.bind_uid);
        bind_phone = findViewById(R.id.bind_phone);
        bind_email = findViewById(R.id.bind_email);
        Button btn_phone = findViewById(R.id.btn_phone);
        btn_email = findViewById(R.id.btn_email);

        btn_phone.setOnClickListener(this);
        btn_email.setOnClickListener(this);
    }

    //初始化数据
    private void initData() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);

        bind_uid.setText(getString(R.string.bind_uid, user.getObjectId()));
        bind_phone.setText(getString(R.string.bind_phone_num,
                user.getMobilePhoneNumber().replaceAll(StaticClass.FORMAT_PHONE_REGEX,
                        "$1****$2")));

        if (!TextUtils.isEmpty(user.getEmail()) && user.getEmail() != null) {
            hasBindEmail = true;
            bind_email.setText(getString(R.string.bind_phone_num, user.getEmail()));
            btn_email.setBackgroundColor(getColor(R.color.bind_gray));
            btn_email.setText(getString(R.string.bind_change));
        } else {
            hasBindEmail = false;
            bind_email.setText("");
            btn_email.setBackgroundColor(getColor(R.color.bind_yellow));
            btn_email.setText(getString(R.string.bind_bind));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone:

                break;
            case R.id.btn_email:
                if (hasBindEmail) {
                    UtilTools.toast(this, "修改邮箱");
                } else {
                    startActivity(new Intent(this,BindEmailActivity.class));
                }
                break;
        }
    }
}
