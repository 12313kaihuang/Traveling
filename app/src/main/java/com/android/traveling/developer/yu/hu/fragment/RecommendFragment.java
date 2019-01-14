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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.NewsAdaptor;
import com.android.traveling.developer.yu.hu.gson.News;
import com.android.traveling.developer.yu.hu.gson.ResultNews;
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.MyOkhttp;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  推荐
 */

public class RecommendFragment extends Fragment {

    private List<News> newsList;
    private NewsAdaptor newsAdaptor;
    private ListView recommend_listView;
    private FrameLayout load_progressbar;

    private TextView tv_load_faild;
    private LinearLayout loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("RecommendFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        initView(view);
        return view;
    }

    //初始化View
    private void initView(View view) {

        load_progressbar = view.findViewById(R.id.load_progressbar);
        tv_load_faild = view.findViewById(R.id.tv_load_faild);
        loading = view.findViewById(R.id.loading);

        recommend_listView = view.findViewById(R.id.recommend_listView);
        SmartRefreshLayout refreshLayout1 = view.findViewById(R.id.recommend_refreshLayout);

        //解析数据
        newsList = new ArrayList<>();
        sendHttpRequest();

        //点击事件
        recommend_listView.setOnItemClickListener((parent, view1, position, id) -> {
            News news = newsList.get(position);
            Intent intent = new Intent(getContext(), NewsActivity.class);
            intent.putExtra("news", news);
            startActivity(intent);
        });


        //上拉刷新
        //noinspection Convert2Lambda
        refreshLayout1.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(() -> {
                    //                                    for (int i = 0; i < 5; i++) {
                    //                                        News news = new News();
                    //                                        news.setTitle("刷新的item" + i);
                    //                                        newsList.add(0, news);
                    //                                    }
                    //                                    newsAdaptor.notifyDataSetChanged();
                    refreshLayout.finishRefresh(false);
                }, 500);
            }
        });

        //下拉加载更多
        refreshLayout1.setOnLoadMoreListener(this::LoadMore);

        //加载失败 重新加载
        tv_load_faild.setOnClickListener(v -> {
            tv_load_faild.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            sendHttpRequest();
        });
    }

    //加载更多
    private void LoadMore(RefreshLayout refreshLayout) {
        String url = StaticClass.LOAD_MORE_NEWS + "?lastId=" + newsList.get(newsList.size()-1).getId();
        System.out.println("lastId:"+newsList.get(newsList.size()-1).getId());
        MyOkhttp.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    refreshLayout.finishLoadMore(false);    //加载失败
                } else {
                    LogUtil.e("RecommendFragment LoadMore() onFailure getActivity = null");
                }

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //noinspection ConstantConditions
                String result = response.body().string();
                try {
                    //在调用了response.body().string()方法之后，response中的流会被关闭
                    JSONObject jsonObject = new JSONObject(result);
                    ResultNews resultNews = new Gson().fromJson(jsonObject.toString(),
                            ResultNews.class);
                    if (getActivity() != null) {
                        if (resultNews.getStatus() == 1) {
                            getActivity().runOnUiThread(() -> {
                                newsList.addAll(resultNews.getNewsList());
                                newsAdaptor.notifyDataSetChanged();
                                refreshLayout.finishLoadMore();
                            });
                        } else {
                            getActivity().runOnUiThread(() -> {
                                refreshLayout.finishLoadMore(false);    //加载失败
                            });
                        }
                    } else {
                        LogUtil.e("RecommendFragment getActivity = null");
                    }

                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //网络请求数据
    private void sendHttpRequest() {
        MyOkhttp.get(StaticClass.LASTEST_NEWS, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        loading.setVisibility(View.INVISIBLE);
                        tv_load_faild.setVisibility(View.VISIBLE);
                        UtilTools.toast(getContext(), "加载失败，请检查您的网络是否通畅");
                    });
                }else {
                   LogUtil.e("sendHttpRequest onFailure getActivity() = null");
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //noinspection ConstantConditions
                String result = response.body().string();
                LogUtil.d(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    ResultNews resultNews = new Gson().fromJson(jsonObject.toString(), ResultNews.class);

                    //状态正确
                    if (resultNews.getStatus() == 1) {
                        newsList = resultNews.getNewsList();

                        //noinspection ConstantConditions
                        getActivity().runOnUiThread(() -> {
                            newsAdaptor = new NewsAdaptor(getActivity(), newsList);
                            recommend_listView.setAdapter(newsAdaptor);
                            load_progressbar.setVisibility(View.INVISIBLE);
                        });
                    } else {
                        UtilTools.toast(getContext(), "返回数据状态有误");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
