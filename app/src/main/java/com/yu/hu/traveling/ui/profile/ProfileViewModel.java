package com.yu.hu.traveling.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.UserService;
import com.yu.hu.traveling.ui.companion.CompanionListFragment;
import com.yu.hu.traveling.ui.home.NoteListFragment;
import com.yu.hu.traveling.ui.home.NoteViewModel;
import com.yu.hu.traveling.utils.UserManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/24 15:34
 **/
public class ProfileViewModel extends AndroidViewModel {
    private final String[] tabs;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        tabs = application.getResources().getStringArray(R.array.profile_tabs);
    }

    FragmentStateAdapter createTabAdapter(Fragment fragment) {
        return createTabAdapter(fragment, UserManager.get().getUserId());
    }

    FragmentStateAdapter createTabAdapter(Fragment fragment, int targetUserId) {
        return new FragmentStateAdapter(fragment) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return NoteListFragment.newInstance(NoteViewModel.TAG_SELF_NOTE).setTargetUserId(targetUserId);
                    case 1:
                        return NoteListFragment.newInstance(NoteViewModel.TAG_SELF_STRATEGY).setTargetUserId(targetUserId);
                    case 2:
                        return CompanionListFragment.newInstance(CompanionListFragment.TAG_TYPE_SELF).setTargetUserId(targetUserId);
                }
                return NoteListFragment.newInstance(NoteViewModel.TAG_SELF_NOTE);
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        };
    }

    @SuppressWarnings("WeakerAccess")
    public void getUserLikeCount(int userId, UserLikeCountCallback callback) {
        ApiService.create(UserService.class)
                .getUserLikeCount(userId)
                .enqueue(new ApiResponseCallback<Integer>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Integer>> call, @NotNull Integer response) {
                        callback.onGetUserLikeCount(response);
                    }
                });
    }

    public TabLayoutMediator.TabConfigurationStrategy create() {
        return (tab, position) -> tab.setText(tabs[position]);
    }

    public interface UserLikeCountCallback {
        void onGetUserLikeCount(int likeCount);
    }
}
