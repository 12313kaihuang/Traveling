package com.android.traveling.developer.yu.hu.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.entity.News;
import com.android.traveling.util.LogUtil;

import java.util.List;

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
            viewHolder.title = convertView.findViewById(R.id.recommend_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        news = newsList.get(position);
        //viewHolder.xx.setText(news.getImgUrl())
        if (news.getTitle() != null) {
            viewHolder.title.setText(news.getTitle());
        }else {
            viewHolder.title.setText(R.string.list_item_title);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView title;
    }
}
