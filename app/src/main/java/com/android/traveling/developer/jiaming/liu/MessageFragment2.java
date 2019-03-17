package com.android.traveling.developer.jiaming.liu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.leancloud.CustomUserProvider;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.fragment.BaseFragment;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationsQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;


import java.util.List;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu
 * 文件名：MessageFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 修改者：LJM
 * 修改时间：2018/9/30 9:00
 * 描述：  消息
 */

public class MessageFragment2 extends BaseFragment {

    private TextView tv_need_login;
    private FrameLayout frameLayout;
    private LCIMConversationListFragment conversationListFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message2, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_need_login = view.findViewById(R.id.tv_need_login);
        frameLayout = view.findViewById(R.id.chat_framelayout);
        registerReceiver();  //注册广播接收器


        //显示/隐藏聊天界面
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null) {
            CustomUserProvider.refreshCacheUser(currentUser);
        }
        if (currentUser == null) {
            conversationListFragment = null;
            tv_need_login.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.INVISIBLE);
            LogUtil.d("MessageFragment2 currentUser == null");
        } else {
            if (conversationListFragment == null) {
                conversationListFragment = new LCIMConversationListFragment();
            }
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.chat_framelayout, conversationListFragment);
            fragmentTransaction.commit();
            tv_need_login.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            setConversationList(currentUser, conversationListFragment);
            LogUtil.d("MessageFragment2 currentUser != null");
        }
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            User currentUser = TravelingUser.getCurrentUser();
            if (currentUser == null) {
                //退出登录
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.remove(conversationListFragment);
                fragmentTransaction.commitAllowingStateLoss();
                conversationListFragment = null;
                tv_need_login.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
                LogUtil.d("MessageFragment2 mReceiver currentUser == null");
            } else {
                //登录
                if (conversationListFragment == null) {
                    conversationListFragment = new LCIMConversationListFragment();
                }
                //未登录 --> 登录
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.chat_framelayout, conversationListFragment);
                fragmentTransaction.commitAllowingStateLoss();  //这里直接commit会报错
                tv_need_login.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                setConversationList(currentUser, conversationListFragment);
                LogUtil.d("MessageFragment2 mReceiver currentUser != null");
            }
        }
    };

    private static void setConversationList(User currentUser, LCIMConversationListFragment fragment) {
        AVIMClient client = AVIMClient.getInstance(String.valueOf(currentUser.getUserId()));
        AVIMConversationsQuery query = client.getConversationsQuery();
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> convs, AVIMException e) {
                if (e == null) {
                    LogUtil.d("setConversationList size=" + convs.size());
                    //convs就是获取到的conversation列表
                    //注意：按每个对话的最后更新日期（收到最后一条消息的时间）倒序排列
                    fragment.setDataList(convs);
                }
            }
        });
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        //注册receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticClass.BROADCAST_LOGIN);
        filter.addAction(StaticClass.BROADCAST_LOGOUT);
        //noinspection ConstantConditions
        getActivity().registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("MessageFragment2 onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.unregisterReceiver(myBroadcastReceiver);
        }
    }
}
