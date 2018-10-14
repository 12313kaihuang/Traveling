package com.android.traveling.developer.yu.hu.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.gson.News;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.news_item, null);
            //初始化viewHolder
            initView(convertView, viewHolder);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //加载View
        News news = newsList.get(position);
        LoadViewData(viewHolder, news, position);

        //添加点击事件
        addEvent(viewHolder, news);

        return convertView;
    }

    //加载界面数据
    private void LoadViewData(ViewHolder viewHolder, News news, int p) {
        //viewHolder.xx.setText(news.getImgUrl())
        viewHolder.tv_username.setText(news.getReleasePeople().getNickName());
        viewHolder.tv_time.setText(news.getTime());
        viewHolder.tv_level.setText(String.format(context.getString(R.string.level), "LV."
                , news.getReleasePeople().getLevel()));
        viewHolder.news_item_like_num.setText(String.valueOf(news.getLike()));
        viewHolder.news_item_commit_num.setText(String.valueOf(news.getComments()));
        viewHolder.list_item_content.setText(String.format(context.getString(R.string.content),
                news.getTitle(), news.getContent()));
        Picasso.get().load(news.getReleasePeople().getImgUrl()).into(viewHolder.user_bg);
        Picasso.get().load(news.getImgList().get(0).getUrl()).into(viewHolder.list_item_icon);

        //是否已关注
        setFocus(viewHolder, news.getReleasePeople().isFocus());
        //设置news类别
        LogUtil.d("position:" + p + " flag:" + news.getFlag());
        setFlag(viewHolder, news.getFlag());
        notifyDataSetChanged();
    }

    //设置news类别
    private void setFlag(ViewHolder viewHolder, int flag) {
        LogUtil.d("flag:" + flag);
        switch (flag) {
            case 1:
                viewHolder.tv_flag.setText(R.string.news_flag1);
                viewHolder.tv_flag.setTextColor(StaticClass.NEWS_FLAG1_COLOR);
                viewHolder.tv_flag.setBackgroundResource(R.drawable.news_flag1_bg);
                break;
            case 2:
                viewHolder.tv_flag.setText(R.string.news_flag2);
                viewHolder.tv_flag.setTextColor(StaticClass.NEWS_FLAG2_COLOR);
                viewHolder.tv_flag.setBackgroundResource(R.drawable.news_flag2_bg);
                break;
            case 3:
                viewHolder.tv_flag.setText(R.string.news_flag3);
                viewHolder.tv_flag.setTextColor(StaticClass.NEWS_FLAG3_COLOR);
                viewHolder.tv_flag.setBackgroundResource(R.drawable.news_flag3_bg);
                break;
        }
    }

    //设置是否关注 isFocus为true则设置成已关注
    private void setFocus(ViewHolder viewHolder, boolean isFocus) {
        viewHolder.tv_focus.setTextSize(12);
        if (isFocus) {
            viewHolder.tv_focus.setText(context.getString(R.string.news_item_focus_on));
            viewHolder.tv_focus.setTextColor(StaticClass.FOCUS_ON_TEXT_COLOR);
            viewHolder.tv_focus.setBackgroundResource(R.drawable.news_item_focus_bg2);
        } else {
            viewHolder.tv_focus.setText(context.getString(R.string.news_item_focus));
            viewHolder.tv_focus.setTextColor(Color.BLACK);
            viewHolder.tv_focus.setBackgroundResource(R.drawable.news_item_focus_bg);
        }

    }

    //初始化View
    private void initView(View convertView, ViewHolder viewHolder) {
        viewHolder.user_bg = convertView.findViewById(R.id.user_bg);
        viewHolder.tv_username = convertView.findViewById(R.id.tv_username);
        viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
        viewHolder.tv_level = convertView.findViewById(R.id.tv_level);
        viewHolder.tv_focus = convertView.findViewById(R.id.tv_focus);
        viewHolder.news_item_like = convertView.findViewById(R.id.news_item_like);
        viewHolder.news_item_like_num = convertView.findViewById(R.id.news_item_like_num);
        viewHolder.news_item_commit_num = convertView.findViewById(R.id.news_item_commit_num);
        viewHolder.list_item_content = convertView.findViewById(R.id.list_item_content);
        viewHolder.list_item_icon = convertView.findViewById(R.id.list_item_icon);

        viewHolder.tv_flag = convertView.findViewById(R.id.news_flag);

    }

    //设置点击事件
    private void addEvent(ViewHolder viewHolder, News news) {
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
        viewHolder.news_item_like_num.setOnClickListener(v ->
                viewHolder.news_item_like.callOnClick());


        //关注 未关注的点击事件
        viewHolder.tv_focus.setOnClickListener(v -> {
            setFocus(viewHolder, !news.getReleasePeople().isFocus());
            if (news.getReleasePeople().isFocus()) {
                news.getReleasePeople().setFocus(false);
            } else {
                news.getReleasePeople().setFocus(true);
            }
        });

    }


    class ViewHolder {
        CircleImageView user_bg;
        ImageView list_item_icon;
        TextView tv_username;
        TextView tv_time;
        TextView tv_level;
        TextView tv_focus;
        TextView tv_flag;
        TextView list_item_content;


        ImageView news_item_like;
        TextView news_item_like_num;
        TextView news_item_commit_num;
        Boolean isLiked = false;
    }
}
