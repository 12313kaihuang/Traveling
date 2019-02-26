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
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.msg.NoteMsg;
import com.android.traveling.entity.note.Note;
import com.android.traveling.entity.note.NoteService;
import com.android.traveling.util.BinarySearch;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  推荐
 */

public class RecommendFragment extends Fragment {

    private List<Note> noteList;
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
        noteList = new ArrayList<>();
        sendHttpRequest();

        //点击事件
        recommend_listView.setOnItemClickListener((parent, view1, position, id) -> {
            Note note = noteList.get(position);
            Intent intent = new Intent(getContext(), NewsActivity.class);
            intent.putExtra(NewsActivity.s_NOTE, note);
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
                    //                                        noteList.add(0, news);
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
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取NoteService对象
        NoteService noteService = retrofit.create(NoteService.class);
        Call<NoteMsg> call = noteService.loadMore(noteList.get(noteList.size() - 1).getId());
        call.enqueue(new Callback<NoteMsg>() {
            @Override
            public void onResponse(@NonNull Call<NoteMsg> call, @NonNull Response<NoteMsg> response) {
                NoteMsg noteMsg = response.body();
                if (noteMsg == null) {
                    LogUtil.e("noteMsg = null");
                } else {
                    if (getActivity() != null) {
                        if (noteMsg.getStatus() == Msg.correctStatus) {
                            getActivity().runOnUiThread(() -> {
                                noteList.addAll(noteMsg.getNotes());
                                newsAdaptor.notifyDataSetChanged();
                                refreshLayout.finishLoadMore();
                            });
                        } else {
                            getActivity().runOnUiThread(() -> {
                                refreshLayout.finishLoadMore(false);    //加载失败
                            });
                        }
                    } else {
                        LogUtil.e("LoadMore = null");
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<NoteMsg> call, @NonNull Throwable t) {
                LogUtil.e("LoadMore onFailure t=" + t);
                if (getActivity() != null) {
                    refreshLayout.finishLoadMore(false);    //加载失败
                }
            }
        });
    }

    //网络请求数据
    private void sendHttpRequest() {
        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取NoteService对象
        NoteService noteService = retrofit.create(NoteService.class);
        Call<NoteMsg> call = noteService.getNewest();
        call.enqueue(new retrofit2.Callback<NoteMsg>() {
            @Override
            public void onResponse(@NonNull Call<NoteMsg> call, @NonNull retrofit2.Response<NoteMsg> response) {
                NoteMsg noteMsg = response.body();
                LogUtil.d("notemsg=" + noteMsg);
                if (noteMsg == null) {
                    LogUtil.e("noteMsg == null");
                } else {
                    if (noteMsg.getStatus() == Msg.errorStatus) {
                        UtilTools.toast(getContext(), "返回数据状态有误");
                    } else {
                        noteList = noteMsg.getNotes();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                newsAdaptor = new NewsAdaptor(getActivity(), noteList);
                                recommend_listView.setAdapter(newsAdaptor);
                                load_progressbar.setVisibility(View.INVISIBLE);
                            });
                        } else {
                            LogUtil.e("getActivity() == null");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NoteMsg> call, @NonNull Throwable t) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        loading.setVisibility(View.INVISIBLE);
                        tv_load_faild.setVisibility(View.VISIBLE);
                        UtilTools.toast(getContext(), "加载失败，t=" + t);
                        LogUtil.e("加载失败，t=" + t);
                        t.printStackTrace();
                    });
                } else {
                    LogUtil.e("加载失败，t=" + t);
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
