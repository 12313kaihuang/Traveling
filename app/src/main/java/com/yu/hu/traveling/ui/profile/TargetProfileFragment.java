package com.yu.hu.traveling.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.utils.TravelingUserProvider;
import com.yu.hu.traveling.utils.UserManager;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * @author Hy
 * created on 2020/05/07 16:14
 * <p>
 * 指定用户的个人详情页
 **/
@FragmentDestination(pageUrl = "user/profile/target")
public class TargetProfileFragment extends ProfileFragment {

    public static final String PAGE_URL = "user/profile/target";

    private int targetUserId;

    private static final String KEY_TARGET_USER = "key_target_user_id";
    private static final String TAG = "TargetProfileFragment";

    public static Bundle createArgs(User user) {
        return createArgs(0, user);
    }

    public static Bundle createArgs(int initPosition, User user) {
        Bundle args = new Bundle();
        args.putInt(KEY_INIT_POSITION, initPosition);
        args.putParcelable(KEY_TARGET_USER, user);
        return args;
    }

    @Override
    protected void beforeInitView() {
        super.beforeInitView();
        Bundle arguments = getArguments();
        if (arguments != null) {
            initPosition = arguments.getInt(KEY_INIT_POSITION, 0);
            User targetUser = arguments.getParcelable(KEY_TARGET_USER);
            if (targetUser != null) {
                targetUserId = targetUser.id;
                mDataBinding.setUser(targetUser);
            }
        } else {
            targetUserId = UserManager.get().getUserId();
        }
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mViewModel.getUserLikeCount(targetUserId, likeCount -> {
            User user = mDataBinding.getUser();
            user.likeCount = likeCount;
            mDataBinding.authorInfoLayout.setUser(user);
        });
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mDataBinding.authorInfoLayout.chatBtn.setOnClickListener(v -> toChatPage());
    }

    private void toChatPage() {
        try {
            // 点击联系人，直接跳转进入聊天界面
            Intent intent = new Intent(mActivity, LCIMConversationActivity.class);
            // 传入对方的 Id 即可
            LogUtil.d(TAG, "targetUserId - " + targetUserId);
            intent.putExtra(LCIMConstants.PEER_ID, String.valueOf(targetUserId));
            TravelingUserProvider.addUser(mDataBinding.getUser());
            TravelingUserProvider.addUser(UserManager.get().getUser());
            mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "LeanCloud开启聊天异常");
        }
    }

    @Override
    protected FragmentStateAdapter getTabAdapter() {
        return mViewModel.createTabAdapter(this, targetUserId);
    }
}
