package com.yu.hu.traveling.ui.my;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentMyBinding;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.ui.behavior.UserBehaviorFragment;
import com.yu.hu.traveling.ui.profile.ProfileFragment;
import com.yu.hu.traveling.utils.UserManager;

/**
 * @author Hy
 * created on 2020/04/14 23:17
 * <p>
 * 我的
 **/
@SuppressWarnings("unused")
@FragmentDestination(pageUrl = "main/tabs/my", tabPage = true, iconRes = R.drawable.icon_tab_my)
public class MyFragment extends BaseFragment<FragmentMyBinding> {

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        mDataBinding.setUser(UserManager.get().getUser());
        UserManager.get().addObserver(this, user -> mDataBinding.setUser(user));

        //刷新用户信息
        if (UserManager.get().isLogin()) {
            LiveData<User> refresh = UserManager.get().refresh();
            refresh.observe(this, user -> {
                LogUtil.d("MyFragment refreshUser - " + user);
                mDataBinding.setUser(user);
                refresh.removeObservers(MyFragment.this);
            });
        }
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mDataBinding.name.setOnClickListener(v -> toProfilePage(0));
        mDataBinding.avatar.setOnClickListener(v -> toProfilePage(0));
        mDataBinding.description.setOnClickListener(v -> toProfilePage(0));
        mDataBinding.goDetail.setOnClickListener(v -> toProfilePage(0));

        mDataBinding.profileLayout.userNote.setOnClickListener(v -> toProfilePage(0));
        mDataBinding.profileLayout.userCompanion.setOnClickListener(v -> toProfilePage(2));
        mDataBinding.profileLayout.userFavorite.setOnClickListener(v -> toBehaviorPage(UserBehaviorFragment.TAG_COLLECT));
        mDataBinding.profileLayout.userHistory.setOnClickListener(v -> toBehaviorPage(UserBehaviorFragment.TAG_HISTORY));

        mDataBinding.loginBtn.setOnClickListener(v -> login());

        //退出登录
        mDataBinding.actionLogout.setOnClickListener(v -> new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.fragment_my_logout))
                .setPositiveButton(getString(R.string.fragment_my_logout_ok), (dialog, which) -> {
                    dialog.dismiss();
                    ToastUtils.showShort("已退出登录");
                    UserManager.get().logout();
                    mDataBinding.setUser(null);
                }).setNegativeButton(getString(R.string.fragment_my_logout_cancel), null)
                .create().show());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDataBinding.setUser(UserManager.get().getUser());
    }

    private void login() {
        UserManager.get().login(mActivity).observe(this, user -> {
            LogUtil.d("login 登录成功 - " + user);
            mDataBinding.setUser(user);
        });
    }

    private void toBehaviorPage(String tag) {
        if (checkLogin()) {
            ((MainActivity) mActivity).navigate(UserBehaviorFragment.PAGE_URL, UserBehaviorFragment.createArgs(tag));
        }
    }

    private void toProfilePage(int initialIndex) {
        if (checkLogin()) {
            ((MainActivity) mActivity).navigate(ProfileFragment.PAGE_URL, ProfileFragment.createArgs(initialIndex));
        }
    }

    private boolean checkLogin() {
        if (UserManager.get().isLogin()) return true;
        login();
        return false;
    }

}
