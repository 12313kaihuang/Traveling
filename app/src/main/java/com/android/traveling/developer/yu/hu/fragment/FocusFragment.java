package com.android.traveling.developer.yu.hu.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.view.View;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.NewsAdapter;
import com.android.traveling.developer.yu.hu.ui.NewsActivity;
import com.android.traveling.entity.note.Note;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  关注
 */

public class FocusFragment extends NewFragment {


    /**
     * 请求文章
     */
    @Override
    protected void sendHttpRequest() {
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser == null) {
            notLogin();
            return;
        }

        LogUtil.d("FocusFragment  userId = " + currentUser.getUserId());

        //获取关注人文章
        Note.getFocusedNewest(currentUser.getUserId(), new Note.Callback() {
            @Override
            public void onSuccess(List<Note> noteList) {
                LogUtil.d("noteList.size() == " + noteList.size());
                if (noteList.size() == 0) {
                    loading.setVisibility(View.INVISIBLE);
                    tv_load_faild.setText("暂无文章");
                    tv_load_faild.setVisibility(View.VISIBLE);
                    UtilTools.toast(getContext(), "暂无关注文章");
                }else {
                    FocusFragment.this.noteList = noteList;
                    newsAdapter = new NewsAdapter(getActivity(), noteList);
                    recommend_listView.setAdapter(newsAdapter);
                    load_progressbar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(String reason) {
                loading.setVisibility(View.INVISIBLE);
                tv_load_faild.setText(getResources().getString(R.string.load_faild));
                tv_load_faild.setVisibility(View.VISIBLE);
                UtilTools.toast(getContext(), "加载失败:" + reason);
            }
        });
    }

    //未登录状态
    private void notLogin() {
        loading.setVisibility(View.INVISIBLE);
        tv_load_faild.setText("未登录");
        tv_load_faild.setVisibility(View.VISIBLE);
        UtilTools.toast(getContext(), "登录之后才可显示关注人文章");
    }


    /**
     * 加载更多
     *
     * @param refreshLayout refreshLayout
     */
    @Override
    protected void LoadMore(RefreshLayout refreshLayout) {

        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser == null) {
            notLogin();
            return;
        }

        Note.loadMoreFocused(currentUser.getUserId(), noteList.get(noteList.size() - 1).getId(), new Note.Callback() {
            @Override
            public void onSuccess(List<Note> noteList) {
                if (noteList.size() == 0) {
                    UtilTools.toast(getContext(), "没有更多文章了");
                    refreshLayout.finishLoadMore();
                    return;
                }
                FocusFragment.this.noteList.addAll(noteList);
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

    /**
     * 刷新
     * @param refreshLayout refreshLayout
     */
    @Override
    protected void refresh(@NonNull RefreshLayout refreshLayout) {
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser == null) {
            notLogin();
            return;
        }

        Note.getFocusedNewest(currentUser.getUserId(), new Note.Callback() {
            @Override
            public void onSuccess(List<Note> noteList) {
                FocusFragment.this.noteList = noteList;
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

    @Override
    protected void registerReceiver() {
        //注册receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticClass.BROADCAST_LOGIN);
        filter.addAction(StaticClass.BROADCAST_LOGOUT);
        if (getActivity() != null) {
            BroadcastReceiver loginOrLogoutReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                   sendHttpRequest();
                }
            };
            getActivity().registerReceiver(loginOrLogoutReceiver, filter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //调用NewsFragment.super.onActivityResult 屏蔽它自己的操作
        superOnActivityResult(requestCode, resultCode, data);
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
        }
    }
}
