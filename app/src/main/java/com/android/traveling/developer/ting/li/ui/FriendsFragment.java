package com.android.traveling.developer.ting.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.traveling.MainActivity;
import com.android.traveling.R;
import com.android.traveling.developer.ting.li.adaptor.FriendsAdaptor;
import com.android.traveling.developer.ting.li.entity.FriendsNews;
import com.android.traveling.developer.yu.hu.adaptor.NewsAdaptor;
import com.android.traveling.developer.yu.hu.entity.News;
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.fragment.BaseFragment;
import com.android.traveling.util.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.fragment
 * 文件名：FriendsFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  结伴
 */

public class FriendsFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        initView(view);
        return view;
    }
    //初始化View
    private void initView(View view) {
        ListView friends_listView = view.findViewById(R.id.friends_news_listView);
        SmartRefreshLayout refreshLayout1 = view.findViewById(R.id.friends_refreshLayout);

        //解析数据
        List<FriendsNews> friendsNewsList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            FriendsNews friendsNews = new FriendsNews();
            friendsNewsList.add(friendsNews);
        }

        FriendsAdaptor friendsAdaptor = new FriendsAdaptor(getActivity(), friendsNewsList);
        friends_listView.setAdapter(friendsAdaptor);
        //点击事件
        friends_listView.setOnItemClickListener((parent, view2, position, id) -> {
            FriendsNews friendsNews = friendsNewsList.get(position);
            Intent intent = new Intent(getContext(), FriendsNewsActivity.class);
            //intent.putExtra("friendsnews",friendsNews);
            startActivity(intent);
        });

        //上拉刷新
        //noinspection Convert2Lambda
        refreshLayout1.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> {
                    for (int i = 0; i < 5; i++) {
                        FriendsNews friendsNews = new FriendsNews();
                        friendsNews.setUserName("刷新的item" + i);
                        friendsNewsList.add(0,friendsNews);
                    }
                    friendsAdaptor.notifyDataSetChanged();
                    refreshLayout.finishRefresh();
                }, 1000);
            }
        });

        //下拉加载更多
        refreshLayout1.setOnLoadMoreListener(refreshLayout -> new Handler().postDelayed(() -> {
            for (int i = 0; i < 5; i++) {
                FriendsNews friendsNews = new FriendsNews();
                friendsNews.setUserName("加载的更多的item" + i);
                friendsNewsList.add(friendsNews);
            }
            friendsAdaptor.notifyDataSetChanged();
            refreshLayout.finishLoadMore();
        }, 1000));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
