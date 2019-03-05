package com.android.traveling.developer.zhiming.li.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：UserEditActivity
 * 创建者：HY
 * 创建时间：2018/10/12 19:50
 * 描述：  编辑个人资料
 */

public class UserEditActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
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

    //初始化View
    private void initView() {
        user = TravelingUser.getCurrentUser();
        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        live_area = findViewById(R.id.live_area);
        signature = findViewById(R.id.signature);
        ImageView img_back = findViewById(R.id.img_back);
        TextView save = findViewById(R.id.save);
        save.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }


    //初始化数据
    private void initData() {

        username.setText(user.getNickName());
        signature.setText(user.getSignature());
        live_area.setText(user.getArea());
        gender.setText(user.getGender());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                User newUser = TravelingUser.getCurrentUser();
                if (newUser != null) {
                    newUser.setNickName(username.getText().toString());
                    newUser.setSignature(signature.getText().toString());
                    newUser.setGender(gender.getText().toString());
                    newUser.setArea(live_area.getText().toString());
                    newUser.update(new Callback<Msg>() {
                        @Override
                        public void onResponse(@NonNull Call<Msg> call, @NonNull Response<Msg> response) {
                            Msg msg = response.body();
                            if (msg != null) {
                                UtilTools.toast(UserEditActivity.this,msg.getInfo());
                                if (msg.getStatus() == Msg.CORRECT_STATUS) {
                                    onBackPressed();
                                }
                            }else {
                                UtilTools.toast(UserEditActivity.this,"出错了 msg为空");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Msg> call, @NonNull Throwable t) {

                            UtilTools.toast(UserEditActivity.this,"出错了 onFailure");
                        }
                    });
                }else {
                    UtilTools.toast(this,"currentUser = null");
                }
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
