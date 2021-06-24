package com.yu.hu.traveling.ui.behavior;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentUserBehaviorBinding;
import com.yu.hu.traveling.ui.home.FocusedNoteListFragment;
import com.yu.hu.traveling.ui.home.NoteViewModel;

/**
 * @author Hy
 * created on 2020/05/05 20:56
 * <p>
 * 收藏/浏览历史
 **/
@FragmentDestination(pageUrl = "user/behavior")
public class UserBehaviorFragment extends BaseFragment<FragmentUserBehaviorBinding> {

    public static final String PAGE_URL = "user/behavior";

    private static final String KEY_TAG = "user_behavior_tag";
    public static final String TAG_COLLECT = "tag_collect";
    public static final String TAG_HISTORY = "tag_history";

    private String tag;

    public static Bundle createArgs(String tag) {
        Bundle args = new Bundle();
        args.putString(KEY_TAG, tag);
        return args;
    }

    @Override
    protected void beforeInitView() {
        super.beforeInitView();
        if (getArguments() != null) {
            tag = getArguments().getString(KEY_TAG);
        }
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        Fragment fragment;
        if (TAG_COLLECT.equals(tag)) {
            mDataBinding.setTitle(getString(R.string.collect));
            fragment = FocusedNoteListFragment.newInstance(NoteViewModel.TAG_COLLECT);
        } else if (TAG_HISTORY.equals(tag)) {
            mDataBinding.setTitle(getString(R.string.fragment_my_history));
            fragment = FocusedNoteListFragment.newInstance(NoteViewModel.TAG_HISTORY);
        } else {
            fragment = FocusedNoteListFragment.newInstance(NoteViewModel.TAG_COLLECT);
        }
        fragmentTransaction.add(R.id.frame_layout_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mDataBinding.actionClose.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_behavior;
    }
}
