package com.android.traveling.developer.jiaming.liu.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Activity
 * 文件名：NoticeActivity
 * 创建者：LJM
 * 创建时间：2018/10/9 19:05
 * 描述：  通知界面
 */
public class NoticeActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton titleback;
    private TextView name;
    private RecyclerView noticeRecycleView;
    private CircleImageView headpicture;
    private TextView reply;
    private ImageView ownContentImage;
    private SmartRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notice);
        initView();
        setAction();
    }
    private void initView(){
        noticeRecycleView = findViewById(R.id.id_notice_noticerecycleview);
        headpicture = findViewById(R.id.id_notice_headpicture);
        reply = findViewById(R.id.id_notice_reply);
        ownContentImage = findViewById(R.id.id_notice_owncontentinmage);
        refreshLayout = findViewById(R.id.id_notice_reflash);
        titleback = findViewById(R.id.id_notice_title).findViewById(R.id.id_titlebar_basic_back);
        name = findViewById(R.id.id_notice_title).findViewById(R.id.id_titlebar_basic_name);
        name.setText("通知");
    }
    private void setAction(){
        headpicture.setOnClickListener(this);
        reply.setOnClickListener(this);
        ownContentImage.setOnClickListener(this);
        titleback.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> {
                    refreshLayout.finishRefresh();
                }, 1000);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_notice_headpicture:{
//                Intent intent = new Intent(this,);
//                startActivity(intent);     //跳转到该用户主页
                break;
            }
            case R.id.id_notice_reply:{
//                Intent intent = new Intent(this,);
//                startActivity(intent);     //跳转到该条帖子
                break;
            }
            case R.id.id_notice_owncontentinmage:{
 //                Intent intent = new Intent(this,);
//                startActivity(intent);     //跳转到该条帖子
                break;
            }
            case R.id.id_titlebar_basic_back:{
                onBackPressed();
                break;
            }
            default:break;
        }
    }
}
