package com.android.traveling.developer.yu.hu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.NewsAdaptor;
import com.android.traveling.developer.yu.hu.entity.News;
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.util.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  推荐
 */

public class RecommendFragment extends Fragment {


    //SmartRefreshLayout
    private SmartRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("RecommendFragment onCreate");
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        initView(view);
        refreshLayout.autoRefresh();    //自动刷新
        return view;
    }

    //初始化View
    private void initView(View view) {
        ListView recommend_listView = view.findViewById(R.id.recommend_listView);
        refreshLayout = view.findViewById(R.id.recommend_refreshLayout);

        //解析数据
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            News news = new News();
            newsList.add(news);
        }

        NewsAdaptor newsAdaptor = new NewsAdaptor(getActivity(), newsList);
        recommend_listView.setAdapter(newsAdaptor);

        //点击事件
        recommend_listView.setOnItemClickListener((parent, view1, position, id) -> {

            startActivity(new Intent(getContext(), NewsActivity.class));
        });

        //noinspection Convert2Lambda
        //上拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> {
                    for (int i = 0; i < 5; i++) {
                        News news = new News();
                        news.setTitle("刷新的item" + i);
                        newsList.add(0,news);
                    }
                    newsAdaptor.notifyDataSetChanged();
                    refreshLayout.finishRefresh();
                }, 1000);
            }
        });

        //下拉加载更多
        refreshLayout.setOnLoadMoreListener(refreshLayout -> new Handler().postDelayed(() -> {
            for (int i = 0; i < 5; i++) {
                News news = new News();
                news.setTitle("加载的更多的item" + i);
                newsList.add(news);
            }
            newsAdaptor.notifyDataSetChanged();
            refreshLayout.finishLoadMore();
        }, 1000));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("RecommendFragment onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("RecommendFragment onDestroy");
    }
}
