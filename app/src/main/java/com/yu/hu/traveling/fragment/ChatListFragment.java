package com.yu.hu.traveling.fragment;

import android.os.Bundle;
import android.view.View;

import com.yu.hu.library.fragment.BaseFragment;
import com.yu.hu.library.util.AppUtil;
import com.yu.hu.library.util.LogUtil;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.entity.bus.BusMessage;
import com.yu.hu.traveling.mvp.impl.ChatListPresence;
import com.yu.hu.traveling.rx.GlobalObserver;
import com.yu.hu.traveling.rx.ViewInitObservable;
import com.yuntongxun.plugin.common.AppMgr;
import com.yuntongxun.plugin.im.ui.chatting.fragment.ConversationListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import rx.Observable;
import rx.functions.Func1;

import static com.yu.hu.traveling.entity.bus.BusMessage.MESSAGE_TYPE_LOGIN;
import static com.yu.hu.traveling.entity.bus.BusMessage.MESSAGE_TYPE_LOGOUT;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2019/6/22 10:44
 * 描述：  聊天列表页面
 */
public class ChatListFragment extends BaseFragment
        implements ChatListPresence {

    private Fragment mChatListFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.frgament_chat_list;
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        //统一在onResume中加载界面
        //onLoadChatList();
    }

    /**
     * 接收登录/登出的广播
     *
     * @param message message
     */
    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAcceptMessage(BusMessage message) {
        LogUtil.d("ChatListFragment -- onAcceptMessage");
        if (message.messageType == MESSAGE_TYPE_LOGOUT) {
            LogUtil.d("退出登录");
            onLoadNotLogin();
        } else if (message.messageType == MESSAGE_TYPE_LOGIN) {
            //登录
            LogUtil.d("当前用户：" + message.user.getNickName());
            onLoadChatList();
        }
    }

    /**
     * 加载聊天列表
     */
    @Override
    public void onLoadChatList() {
        //如果有缓存就加载聊天列表
        if (AppMgr.getClientUser() != null) {

            //显示progressBar
            findViewById(R.id.fragment_container).setVisibility(View.GONE);
            findViewById(R.id.ll_not_login).setVisibility(View.GONE);
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

            //先显示progress_bar，优化体验
            ViewInitObservable.create(() -> {
                //新线程创建fragment
                Bundle bundle = new Bundle();
                bundle.putBoolean(ConversationListFragment.EXTRA_SHOW_TITLE, false);
                return Observable.just(Fragment.instantiate(getContext(),
                        ConversationListFragment.class.getName(), bundle));
            }).subscribe(new GlobalObserver<Fragment>() {
                @Override
                public void onNext(Fragment fragment) {
                    mChatListFragment = fragment;
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    setChildFragment(R.id.fragment_container, mChatListFragment);
                    findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                }
            });

        } else {
            LogUtil.d("未登录");
            onLoadNotLogin();
        }
    }

    /**
     * 加载 没有登录时的页面
     */
    @Override
    public void onLoadNotLogin() {
        if (mChatListFragment != null) {
            LogUtil.d("fragment != null");
            removeChildFragment(mChatListFragment);
            mChatListFragment = null;
        }
        LogUtil.d("fragment = null");
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        findViewById(R.id.fragment_container).setVisibility(View.GONE);
        findViewById(R.id.ll_not_login).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoadChatList();
        LogUtil.d(AppUtil.getLogTag(getClass()) + "--onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(AppUtil.getLogTag(getClass()) + "--onPause");
    }

    /**
     * 新学到的方法 *_*
     * 这样就不需要在Activity中手动调用上onResume，onPause方法了
     *
     * @param hidden true -> 不可见  ，false -> 可见
     */
    @Override
    public void onHiddenChanged(boolean hidden) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
