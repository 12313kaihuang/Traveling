package com.android.traveling.developer.yu.hu.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import com.android.traveling.MainActivity;
import com.android.traveling.R;
import com.android.traveling.developer.jiaming.liu.activity.AddNoteActivity;
import com.android.traveling.developer.yu.hu.adaptor.NewsAdapter;
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.entity.note.Note;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  最新
 */

public class NewFragment extends Fragment {

    private static final int REQUEST_CODE = 0;  //请求码 去NewsActivity
    //    private static final String TAG = "RecommendFragment";

    protected List<Note> noteList;
    protected NewsAdapter newsAdapter;
    protected ListView recommend_listView;
    protected FrameLayout load_progressbar;

    protected TextView tv_load_faild;
    protected LinearLayout loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("RecommendFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        initView(view);
        return view;
    }

    //初始化View
    protected void initView(View view) {

        load_progressbar = view.findViewById(R.id.load_progressbar);
        tv_load_faild = view.findViewById(R.id.tv_load_faild);
        loading = view.findViewById(R.id.loading);

        recommend_listView = view.findViewById(R.id.recommend_listView);
        SmartRefreshLayout refreshLayout1 = view.findViewById(R.id.recommend_refreshLayout);

        //解析数据
        noteList = new ArrayList<>();
        sendHttpRequest();

        //注册receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticClass.BROADCAST_LOGIN);
        filter.addAction(StaticClass.BROADCAST_LOGOUT);
        if (getActivity() != null) {
            BroadcastReceiver loginOrLogoutReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    newsAdapter.notifyDataSetInvalidated(); //重绘整个控件
                }
            };
            getActivity().registerReceiver(loginOrLogoutReceiver, filter);
        }

        //点击事件
        recommend_listView.setOnItemClickListener((parent, view1, position, id) -> {
            Note note = noteList.get(position);
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            intent.putExtra(NewsActivity.POSITION, position);
            intent.putExtra(NewsActivity.s_NOTE, note);
            startActivityForResult(intent, REQUEST_CODE);  //注意 跳转过去后setResult后需Finish()才会正确传回结果来！
        });


        //上拉刷新
        //noinspection Convert2Lambda
        refreshLayout1.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Note.getNewest(new Note.Callback() {
                    @Override
                    public void onSuccess(List<Note> noteList) {
                        NewFragment.this.noteList = noteList;
                        newsAdapter.setNewsList(noteList);
                        newsAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailure(String reason) {
                        refreshLayout.finishRefresh(false);
                        UtilTools.toast(getContext(), "加载失败:" + reason);
                    }
                });
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
    protected void LoadMore(RefreshLayout refreshLayout) {

        Note.loadMore(noteList.get(noteList.size() - 1).getId(), new Note.Callback() {
            @Override
            public void onSuccess(List<Note> noteList) {
                if (noteList.size() == 0) {
                    UtilTools.toast(getContext(), "没有更多文章了");
                    refreshLayout.finishLoadMore();
                    return;
                }
                NewFragment.this.noteList.addAll(noteList);
                newsAdapter.notifyDataSetChanged();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onFailure(String reason) {
                UtilTools.toast(getContext(), "加载失败：" + reason);
                refreshLayout.finishLoadMore(false);
            }
        });

    }

    //网络请求数据
    protected void sendHttpRequest() {

        Note.getNewest(new Note.Callback() {
            @Override
            public void onSuccess(List<Note> noteList) {
                NewFragment.this.noteList = noteList;
                newsAdapter = new NewsAdapter(getActivity(), noteList);
                recommend_listView.setAdapter(newsAdapter);
                load_progressbar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(String reason) {
                loading.setVisibility(View.INVISIBLE);
                tv_load_faild.setVisibility(View.VISIBLE);
                UtilTools.toast(getContext(), "加载失败:" + reason);
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

    @Override
    public void onResume() {
        super.onResume();
        //        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == NewsActivity.RESULT_CODE) {
                    //统一点赞情况
                    Note note = (Note) data.getSerializableExtra(NewsActivity.s_NOTE);
                    int position = data.getIntExtra(NewsActivity.POSITION, -1);
                    if (position >= 0 && position < noteList.size()) {
                        noteList.set(position, note);
                    }
                    newsAdapter.notifyDataSetChanged();
                }
                break;
            //发表游记/攻略
            case MainActivity.REQUEST_CODE_NOTE_TAG1:
            case MainActivity.REQUEST_CODE_NOTE_TAG2:
                if (resultCode == Activity.RESULT_OK) {
                    Note note = (Note)data.getSerializableExtra(AddNoteActivity.INTENT_EXTRA_NOTE);
                    noteList.add(0,note);
                    newsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }
}
