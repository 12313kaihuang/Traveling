package com.android.traveling.developer.jiaming.liu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.jiaming.liu.adapter.ChatMessageAdapter;
import com.android.traveling.developer.jiaming.liu.item.ChatMessageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Activity
 * 文件名：ChatActivity
 * 创建者：LJM
 * 创建时间：2018/9/29 20:19
 * 描述：  聊天界面
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    private List<ChatMessageItem> msgList = new ArrayList<>();
    private EditText inputText;
    private Button sendbtn;
    private RecyclerView msgRecyclerView;
    private ChatMessageAdapter msgAdapter;

    private ImageButton titleMore;
    private LinearLayout titleBack;
    private TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        initView();
        setAction();
        initMsg();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        msgAdapter = new ChatMessageAdapter(msgList);
        msgRecyclerView.setAdapter(msgAdapter);

        }
    private void initView(){
        inputText = findViewById(R.id.id_chat_input_text);
        sendbtn = findViewById(R.id.id_chat_sendmsg);
        msgRecyclerView = findViewById(R.id.id_chat_msgrecycleview);
        titleBack = findViewById(R.id.id_chat_title).findViewById(R.id.id_titlebar_chat_back);
        titleMore = findViewById(R.id.id_chat_title).findViewById(R.id.id_titlebar_chat_more);
        titleName = findViewById(R.id.id_chat_title).findViewById(R.id.id_titlebar_chat_name);
    }
    private void setAction(){
        titleBack.setOnClickListener(this);
        titleMore.setOnClickListener(this);
        sendbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.id_chat_sendmsg:{
                String content = ToDBC(inputText.getText().toString());//把英文数字等改为全角
                if(!"".equals(content)){
                    msgList.add(new ChatMessageItem("aaaa",content,ChatMessageItem.SEND_MESSAGE));
                    msgAdapter.notifyItemInserted(msgList.size()-1);//刷新
                    msgRecyclerView.scrollToPosition(msgList.size()-1);//将recycleview定位到最后一行
                    inputText.setText("");//清空输入框中的内容
                }
                break;
            }
            case R.id.id_titlebar_chat_back:{
                onBackPressed();
                break;
            }
            case R.id.id_titlebar_chat_more:{
                Intent intent = new Intent(this,ChatMoreActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    private void initMsg(){
        ChatMessageItem msg;
        for(int i=0; i<10;i++){
            if(i%2==0){
                msg = new ChatMessageItem("aaaaaaa","发送消息"+i,ChatMessageItem.SEND_MESSAGE);
                msgList.add(msg);
            }else{
                msg = new ChatMessageItem("aaaaaaa","接收消息"+i,ChatMessageItem.RECEIVE_MESSAGE);
                msgList.add(msg);
            }
        }
    }
}
