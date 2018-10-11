package com.android.traveling.developer.ting.li.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.ting.li.entity.FriendsNews;

import java.util.List;

public class FriendsAdaptor extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<FriendsNews> friendsNewsList;
    private FriendsNews friendsNews;

    public FriendsAdaptor(Context context, List<FriendsNews> friendsNewsList) {
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FriendsAdaptor.ViewHolder) convertView.getTag();
        }

        friendsNews = friendsNewsList.get(position);
        //viewHolder.xx.setText(news.getImgUrl())
        if (friendsNews.getUserName() != null) {
            viewHolder.title.setText(friendsNews.getUserName());
        }else {
            viewHolder.title.setText(R.string.list_friends_item_username);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView title;
    }
}
