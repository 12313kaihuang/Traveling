package com.yu.hu.traveling.ui.profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentProfileBinding;
import com.yu.hu.traveling.utils.UserManager;

/**
 * @author Hy
 * created on 2020/04/23 22:38
 * <p>
 * 个人信息详情页
 **/
@FragmentDestination(pageUrl = "user/profile")
public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    public static final String PAGE_URL = "user/profile";

    static final String KEY_INIT_POSITION = "key_init_position";

    public static Bundle createArgs(int initPosition) {
        Bundle args = new Bundle();
        args.putInt(KEY_INIT_POSITION, initPosition);
        return args;
    }

    int initPosition = 0;
    protected ProfileViewModel mViewModel;

    @Override
    protected void beforeInitView() {
        super.beforeInitView();
        Bundle arguments = getArguments();
        if (arguments != null) {
            initPosition = arguments.getInt(KEY_INIT_POSITION, 0);
        }
        mDataBinding.setUser(UserManager.get().getUser());
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mDataBinding.setExpand(false);

        mDataBinding.viewPager.setAdapter(getTabAdapter());
        new TabLayoutMediator(mDataBinding.tabLayout, mDataBinding.viewPager, mViewModel.create())
                .attach();

        mDataBinding.viewPager.post(() -> mDataBinding.viewPager.setCurrentItem(initPosition, false));
    }

    protected FragmentStateAdapter getTabAdapter() {
        return mViewModel.createTabAdapter(this);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);

        mDataBinding.headerLayout.actionBack.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());

        //滑动监听
        mDataBinding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            boolean expand = Math.abs(verticalOffset) < appBarLayout.getTotalScrollRange();
            mDataBinding.setExpand(expand);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }
}
