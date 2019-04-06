package com.android.traveling.developer.jiaming.liu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.traveling.R;
import com.android.traveling.developer.jiaming.liu.activity.ChatActivity;
import com.android.traveling.developer.jiaming.liu.adapter.ChatAdapter;
import com.android.traveling.developer.jiaming.liu.item.ChatItem;
import com.android.traveling.fragment.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu
 * 文件名：MessageFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 修改者：LJM
 * 修改时间：2018/9/30 9:00
 * 描述：  消息
 */

public class MessageFragment extends BaseFragment {
    private View view = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        List<ChatItem> itemList = new ArrayList<>();
        initChatItem(itemList);
        ChatAdapter adapter = new ChatAdapter(getActivity(),R.layout.item_chat,itemList);
        ListView listView = view.findViewById(R.id.id_message_listview);
        SmartRefreshLayout refreshLayout = view.findViewById(R.id.id_message_reflash);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> {
                    refreshLayout.finishRefresh();
                }, 1000);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(),ChatActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               itemList.remove(i);  //长按删除
               adapter.notifyDataSetChanged();
                return true;
            }
        });
        listView.setAdapter(adapter);
    }
    private void initChatItem(List<ChatItem> itemList){
        ChatItem[] chat = new ChatItem[10];
        for(int i=0; i<10; i++){
            chat[i] = new ChatItem();
            chat[i].setHeadPicture(R.drawable.user_bg);
            chat[i].setTxtNewReply("最新一条消息");
            chat[i].setTxtOthersName("别人的昵称");
            chat[i].setTxtChatTime("18:20");
            itemList.add(chat[i]);
        }
    }
}
