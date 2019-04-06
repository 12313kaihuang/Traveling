package com.android.traveling.developer.jiaming.liu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.jiaming.liu.item.NoticeItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Adapter
 * 文件名：NoticeAdapter
 * 创建者：LJM
 * 创建时间：2018/10/9 19:26
 * 描述：  通知列表适配器
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
    private List<NoticeItem> noticeList;
    public NoticeAdapter(List<NoticeItem> noticeList){
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoticeItem noticeItem = noticeList.get(position);
        holder.headpicture.setImageResource(noticeItem.getHeadPicture());
        holder.reply.setText(noticeItem.getReply());
        holder.ownContentImage.setImageResource(noticeItem.getOwnContentImage());
    }
    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView headpicture;
        TextView reply;
        ImageView ownContentImage;
        public ViewHolder(View view) {
            super(view);
            headpicture = view.findViewById(R.id.id_notice_headpicture);
            reply = view.findViewById(R.id.id_notice_reply);
            ownContentImage = view.findViewById(R.id.id_notice_owncontentinmage);
        }
    }
}
