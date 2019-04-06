package com.android.traveling.developer.ting.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.traveling.R;
import com.android.traveling.developer.ting.li.adaptor.FriendsAdaptor;
import com.android.traveling.developer.ting.li.adaptor.FriendsReplyAdaptor;
import com.android.traveling.developer.ting.li.entity.FriendsNews;
import com.android.traveling.entity.comment.Comment;
import com.android.traveling.entity.comment.Reply;
import com.android.traveling.entity.companion.Companion;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.fragment.BaseFragment;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
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
    List<Companion> friendsNewsList;
    FriendsAdaptor friendsAdaptor;
    ListView friends_listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        initView(view);
        return view;
    }
    //初始化View
    private void initView(View view) {
        friends_listView = view.findViewById(R.id.friends_news_listView);
        SmartRefreshLayout refreshLayout1 = view.findViewById(R.id.friends_refreshLayout);
        //解析数据
        friendsNewsList = new ArrayList<>();
        Companion.getNewest(new Companion.Callback() {
            @Override
            public void onSuccess(List<Companion> companions) {
                friendsNewsList.addAll(companions);
                LogUtil.d("222"+companions.get(0).getNickName()+companions.get(0).getImgUrl()+companions.get(0).getContent());
            }
            @Override
            public void onFailure(int errCode, String reason) {
                if (errCode== Msg.NO_DATA){
                }
            }
        });
        friendsAdaptor = new FriendsAdaptor(getActivity(), friendsNewsList);
        friends_listView.setAdapter(friendsAdaptor);
        //点击事件
        friends_listView.setOnItemClickListener((parent, view2, position, id) -> {
            Companion companion = friendsNewsList.get(position);
            companion.setViews(companion.getViews()+1);
            friendsAdaptor.notifyDataSetChanged();
            Intent intent = new Intent(getActivity(), FriendsNewsActivity.class);
            //intent.putExtra(FriendsNewsActivity.ID, id);
            intent.putExtra(FriendsNewsActivity.POSITION, position);
            intent.putExtra(FriendsNewsActivity.s_COMPANION, companion);
            //intent.putExtra(FriendsNewsActivity.COMMENT, comment);
            startActivity(intent);
        });

        //下拉刷新
        refreshLayout1.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Companion.getNewest(new Companion.Callback() {
                    @Override
                    public void onSuccess(List<Companion> companions) {
                        //friendsNewsList.addAll(companions);
                        FriendsFragment.this.friendsNewsList = companions;
                        LogUtil.d("333"+companions.get(0).getNickName()+companions.get(0).getImgUrl()+companions.get(0).getContent());
                        friendsAdaptor = new FriendsAdaptor(getActivity(), companions);
                        friends_listView.setAdapter(friendsAdaptor);
                        refreshLayout.finishRefresh();
                    }
                    @Override
                    public void onFailure(int errCode, String reason) {
                        if (errCode== Msg.NO_DATA){
                            refreshLayout.finishRefresh(false);
                            UtilTools.toast(getContext(), "加载失败:" + reason);
                        }
                    }
                });
            }
        });
        //上拉加载更多
        refreshLayout1.setOnLoadMoreListener(refreshLayout -> new Handler().postDelayed(() -> {
            Companion.loadMore(friendsNewsList.size(), new Companion.Callback() {
                @Override
                public void onSuccess(List<Companion> companions) {
                    friendsNewsList.addAll(companions);
                    friendsAdaptor.notifyDataSetChanged();
                    refreshLayout.finishLoadMore();
                }
                @Override
                public void onFailure(int errCode, String reason) {
                    UtilTools.toast(getContext(), "加载失败：" + reason);
                    refreshLayout.finishLoadMore(false);
                }
            });
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
