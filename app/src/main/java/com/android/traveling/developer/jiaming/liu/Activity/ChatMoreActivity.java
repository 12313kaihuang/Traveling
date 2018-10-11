package com.android.traveling.developer.jiaming.liu.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.traveling.R;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Activity
 * 文件名：ChatMoreActivity
 * 创建者：LJM
 * 创建时间：2018/10/9 20:30
 * 描述：  私聊用户设置界面
 */
public class ChatMoreActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton titleback;
    private TextView name;
    private TextView setChatbg;
    private TextView resetName;
    private TextView findChatLogs;
    private TextView clearChatLogs;
    private TextView follow;
    private TextView pullblack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_more);
        initView();
        setAction();
    }

    private void initView() {
        titleback = findViewById(R.id.id_chatmore_title).findViewById(R.id.id_titlebar_basic_back);
        name = findViewById(R.id.id_chatmore_title).findViewById(R.id.id_titlebar_basic_name);
        setChatbg = findViewById(R.id.id_chatmore_setchatbg);
        findChatLogs = findViewById(R.id.id_chatmore_findchatlogs);
        resetName = findViewById(R.id.id_chatmore_resetname);
        clearChatLogs = findViewById(R.id.id_chatmore_clearchatlogs);
        follow = findViewById(R.id.id_chatmore_follow);
        pullblack = findViewById(R.id.id_chatmore_pullblack);
    }

    private void setAction(){
        titleback.setOnClickListener(this);
        setChatbg.setOnClickListener(this);
        resetName.setOnClickListener(this);
        findChatLogs.setOnClickListener(this);
        clearChatLogs.setOnClickListener(this);
        follow.setOnClickListener(this);
        pullblack.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_titlebar_basic_back:{
                onBackPressed();
                break;
            }
            case R.id.id_chatmore_setchatbg:{
                //设置聊天背景
                break;
            }
            case R.id.id_chatmore_resetname:{
                //设置备注
                name.setText("备注");
                break;
            }
            case R.id.id_chatmore_findchatlogs:{
                //查找聊天记录
                break;
            }
            case R.id.id_chatmore_clearchatlogs:{
                //清空聊天记录
                break;
            }
            case R.id.id_chatmore_follow:{
                //关注
                break;
            }
            case R.id.id_chatmore_pullblack:{
                //拉黑
                break;
            }
            default:break;
        }
    }
}
