package com.android.traveling.developer.ting.li.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.ting.li.entity.FriendsNews;
import com.android.traveling.entity.companion.Companion;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class FriendsAdaptor extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Companion> friendsNewsList;
    private Companion companion;

    public FriendsAdaptor(Context context, List<Companion> friendsNewsList) {
        this.context = context;
        this.friendsNewsList = friendsNewsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return friendsNewsList.size();
    }
    @Override
    public Object getItem(int position) {
        return friendsNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendsAdaptor.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new FriendsAdaptor.ViewHolder();
            convertView = inflater.inflate(R.layout.friends_news_item, null);
            //初始化viewHolder
            //viewHolder.xxx = convertView.findViewById()
            viewHolder.title = convertView.findViewById(R.id.friends_news_username);
            viewHolder.content = convertView.findViewById(R.id.friends_news_content);
            viewHolder.img = convertView.findViewById(R.id.friends_news_img);
            viewHolder.createTime = convertView.findViewById(R.id.friends_news_time_create);
            viewHolder.startTime = convertView.findViewById(R.id.friends_news_time_start);
            viewHolder.endTime = convertView.findViewById(R.id.friends_news_time_end);
            viewHolder.target = convertView.findViewById(R.id.friends_news_time_place);
            viewHolder.views = convertView.findViewById(R.id.friends_news_time_views);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FriendsAdaptor.ViewHolder) convertView.getTag();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        companion = friendsNewsList.get(position);
        //viewHolder.xx.setText(news.getImgUrl())
        if (companion.getNickName() != null) {
            viewHolder.title.setText(companion.getNickName());
        }else {
            viewHolder.title.setText(R.string.list_friends_item_username);
        }
        if (companion.getContent() != null) {
            viewHolder.content.setText(companion.getContent());
        }else {
            viewHolder.content.setText(R.string.list_friends_item_content);
        }
        if (companion.getImgUrl() != null){
            Picasso.get().load(companion.getImgUrl()).into(viewHolder.img);
        }
        if(companion.getCreateTime() != null){
            viewHolder.createTime.setText(format.format(companion.getCreateTime()));
        }
        if(companion.getStartTime() != null){
            viewHolder.startTime.setText(format.format(companion.getStartTime()));
        }
        if(companion.getEndTime() != null){
            viewHolder.endTime.setText(format.format(companion.getEndTime()));
        }
        if(companion.getTarget() != null){
            viewHolder.target.setText(companion.getTarget());
        }
        if(companion.getViews() != null){
            viewHolder.views.setText(companion.getViews().toString()+"浏览");
        }
        return convertView;
    }

    class ViewHolder {
        public TextView title;

        public TextView content;

        public ImageView img;

        public TextView createTime;

        public TextView startTime;

        private TextView endTime;

        private TextView target;

        private TextView views;

    }
}
