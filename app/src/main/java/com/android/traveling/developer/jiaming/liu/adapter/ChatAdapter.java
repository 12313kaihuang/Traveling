package com.android.traveling.developer.jiaming.liu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.jiaming.liu.item.ChatItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Adapter
 * 文件名：ChatAdapter
 * 创建者：LJM
 * 创建时间：2018/9/29 19:12
 * 描述：  聊天列表适配器
 */
public class ChatAdapter extends ArrayAdapter<ChatItem> {
    private List<ChatItem> chatItemsList;
    private int resourceId;

    public ChatAdapter(@NonNull Context context, int resource, @NonNull List<ChatItem> textViewResourceId) {
        super(context, resource, textViewResourceId);
        resourceId = resource;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ChatItem item =  getItem(i);
        ViewHoler viewHoler;
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,viewGroup,false);
            viewHoler = new ViewHoler();
            viewHoler.head = view.findViewById(R.id.id_chat_item_headpicture);
            viewHoler.txtnewReply = view.findViewById(R.id.id_chat_item_newReplytxt);
            viewHoler.txtxontherName = view.findViewById(R.id.id_chat_item_othersnametxt);
            viewHoler.txtchatTime = view.findViewById(R.id.id_chat_item_chattimetxt);
            view.setTag(viewHoler);
        }else{
            view = convertView;
            viewHoler = (ViewHoler) view.getTag();
        }
        viewHoler.head.setImageResource(item.getHeadPicture());
        viewHoler.txtnewReply.setText(item.getTxtNewReply());
        viewHoler.txtxontherName.setText(item.getTxtOthersName());
        viewHoler.txtchatTime.setText(item.getTxtChatTime());
        return view;
    }
    class ViewHoler{
        CircleImageView head;
        TextView txtnewReply;
        TextView txtxontherName;
        TextView txtchatTime;
    }
}
