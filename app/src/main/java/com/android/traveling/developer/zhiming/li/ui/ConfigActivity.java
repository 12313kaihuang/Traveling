package com.android.traveling.developer.zhiming.li.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.zhiming.li.gson.Config;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.MyOkhttp;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：ConfigActivity
 * 创建者：HY
 * 创建时间：2018/10/17 0:11
 * 描述：  关于界面
 */

public class ConfigActivity extends BackableActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initView();
    }

    private void initView() {
        TextView version = findViewById(R.id.version);
        MyOkhttp.get(StaticClass.CHECK_UPDATE_URL, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                UtilTools.toast(ConfigActivity.this,"获取版本信息失败");
            }

            @Override
            public void onResponse(@NonNull Call call,@NonNull  Response response) throws IOException {
                LogUtil.d("成功了");
                try {
                    //noinspection ConstantConditions
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Config config = new Gson().fromJson(jsonObject.toString(), Config.class);
                    LogUtil.d(config.getVersionName());
                    runOnUiThread(() -> version.setText(getString(R.string.version,
                            config.getVersionName())));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
