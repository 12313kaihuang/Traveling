package com.android.traveling.developer.yu.hu.adaptor;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.entity.News;

import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.adaptor
 * 文件名：NewsAdaptor
 * 创建者：HY
 * 创建时间：2018/9/27 15:08
 * 描述：  获取信息列表
 */

public class NewsAdaptor extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<News> newsList;
    private News news;

    public NewsAdaptor(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.news_item, null);
            //初始化viewHolder
            //viewHolder.xxx = convertView.findViewById()
            viewHolder.news_item_like = convertView.findViewById(R.id.news_item_like);
            viewHolder.news_item_like_num = convertView.findViewById(R.id.news_item_like_num);

            //badge
            viewHolder.badge1 = new QBadgeView(context)
                    .bindTarget(convertView.findViewById(R.id.badge_tag1))
                    .setBadgeBackgroundColor(0xFFF8E6A1)
                    .setBadgeTextColor(0xFFFFFFFF)
                    .setBadgePadding(5, true)
                    .setBadgeGravity(Gravity.CENTER)
                    .setBadgeTextSize(12, true)
                    .setBadgeText("游记");


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //喜欢的点击事件
        viewHolder.news_item_like.setOnClickListener(v -> {
            if (viewHolder.isLiked) {
                viewHolder.news_item_like.setBackgroundResource(R.drawable.ic_like);
                int like_num = Integer.parseInt(viewHolder.news_item_like_num.getText().toString());
                viewHolder.news_item_like_num.setText(String.valueOf(like_num - 1));
                viewHolder.isLiked = false;
            } else {
                viewHolder.news_item_like.setBackgroundResource(R.drawable.ic_like2);
                int like_num = Integer.parseInt(viewHolder.news_item_like_num.getText().toString());
                viewHolder.news_item_like_num.setText(String.valueOf(like_num + 1));
                viewHolder.isLiked = true;
            }

        });
        viewHolder.news_item_like_num.setOnClickListener(v -> {
            viewHolder.news_item_like.callOnClick();
        });

        news = newsList.get(position);
        //viewHolder.xx.setText(news.getImgUrl())

        //            viewHolder.title.setText(R.string.list_item_title);

        return convertView;
    }


    class ViewHolder {
        public TextView title;
        Badge badge1;

        ImageView news_item_like;
        TextView news_item_like_num;
        Boolean isLiked = false;
    }
}
