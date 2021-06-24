package com.yu.hu.traveling.ui.chat;

import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.traveling.R;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.databinding.FragmentChatListBinding;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.utils.UserManager;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;
import cn.leancloud.chatkit.utils.ChatKitUtils;

/**
 * @author Hy
 * created on 2020/04/14 23:21
 * <p>
 * 消息页面
 **/
@SuppressWarnings("unused")
@FragmentDestination(pageUrl = "main/tabs/information", tabPage = true, needLogin = true, iconRes = R.drawable.icon_tab_message)
public class ChatListFragment extends BaseFragment<FragmentChatListBinding> {

    private static final String TAG = "ChatListFragment";
    private LCIMConversationListFragment conversationListFragment;

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) refreshView();
    }

    private void refreshView() {
        buildPage(UserManager.get().getUser());
        if (conversationListFragment != null) {
            conversationListFragment.updateConversationList();
        }
    }

    private void buildPage(User user) {
        mDataBinding.setUser(user);
        Log.i(TAG, "buildPage: user = " + user);
        if (user == null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            if (conversationListFragment != null && conversationListFragment.isAdded()) {
                fragmentTransaction.remove(conversationListFragment);
                fragmentTransaction.commit();
            }
            conversationListFragment = null;
        } else {
            if (conversationListFragment == null) {
                conversationListFragment = new LCIMConversationListFragment();
            }
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            if (!conversationListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.chat_frame_layout, conversationListFragment);
            }
            fragmentTransaction.commit();
            ChatKitUtils.setConversationList(String.valueOf(user.id), conversationListFragment);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_list;
    }
}
