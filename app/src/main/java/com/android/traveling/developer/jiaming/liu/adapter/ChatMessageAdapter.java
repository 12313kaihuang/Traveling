package com.android.traveling.developer.jiaming.liu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.jiaming.liu.item.ChatMessageItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Adapter
 * 文件名：ChatMessageAdapter
 * 创建者：LJM
 * 创建时间：2018/9/30 16:32
 * 描述：  聊天消息列表适配器
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>{
    private List<ChatMessageItem> msgList;

    public ChatMessageAdapter(List<ChatMessageItem> msgList){
        this.msgList = msgList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatmessage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessageItem msgItem = msgList.get(position);
        if(msgItem.getType() == ChatMessageItem.RECEIVE_MESSAGE){
            holder.receiveLayout.setVisibility(View.VISIBLE);
            holder.sendLayout.setVisibility(View.GONE);
            holder.receiveMsg.setText(msgItem.getContent());
            //holder.receiveHead.setImageResource(msgItem.getUerId().);//通过聊天用户设置显示聊天人的头像
        }else if(msgItem.getType() == ChatMessageItem.SEND_MESSAGE){
            holder.receiveLayout.setVisibility(View.GONE);
            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.sendMsg.setText(msgItem.getContent());
            //holder.sendHead.setImageResource(msgItem.getUerId().);//通过聊天用户设置显示聊天人的头像
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout receiveLayout;
        LinearLayout sendLayout;
        TextView receiveMsg;
        TextView sendMsg;
        CircleImageView receiveHead;
        CircleImageView sendHead;

        public ViewHolder(View view){
            super(view);
            receiveLayout = view.findViewById(R.id.id_chatmessage_receivelayout);
            sendLayout = view.findViewById(R.id.id_chatmessage_sendlayout);
            receiveHead = view.findViewById(R.id.id_chatmessage_receiveHead);
            sendHead = view.findViewById(R.id.id_chatmessage_sendHead);
            receiveMsg = view.findViewById(R.id.id_chatmessage_receivemsg);
            sendMsg = view.findViewById(R.id.id_chatmessage_sendmsg);
        }
    }
}
