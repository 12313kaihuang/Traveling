package com.yu.hu.traveling.ui.companion;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.paging.DataSource;

import com.blankj.utilcode.util.GsonUtils;
import com.yu.hu.common.paging.RoomViewModel;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.db.repository.CompanionRepository;
import com.yu.hu.traveling.model.Companion;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.CompanionService;
import com.yu.hu.traveling.utils.UserManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/24 12:12
 **/
public class CompanionViewModel extends RoomViewModel<Integer, Companion> {

    private static final String TAG = "CompanionViewModel";

    private CompanionRepository companionRepository;
    private String tag;
    private int targetUserId; //用于跳转他人的个人页面使用

    @SuppressWarnings("WeakerAccess")
    public CompanionViewModel(Application app, String tag) {
        super(app);
        this.tag = tag;
        this.targetUserId = UserManager.get().getUserId();
        this.companionRepository = new CompanionRepository();
    }

    @Override
    public DataSource.Factory<Integer, Companion> createDataSourceFactory() {
        if (CompanionListFragment.TAG_TYPE_SELF.equals(tag)) {
            LogUtil.d("testsss - " + targetUserId);
            return companionRepository.getTargetCompanionFactory(targetUserId);
        }
        return companionRepository.getFactory();
    }

    @Override
    public void refresh() {
        loadData(Integer.MAX_VALUE);
    }

    @Override
    public void loadData(Integer companionId) {
        LogUtil.d(TAG, "loadData - startId = " + companionId);
        if (companionId == null) {
            companionId = Integer.MAX_VALUE;
        }
        Call<ApiResponse<List<Companion>>> call;
        if (targetUserId != 0 && CompanionListFragment.TAG_TYPE_SELF.equals(tag)) {
            LogUtil.d(" testsss2, targetUserId = "+targetUserId);
            call = ApiService.create(CompanionService.class)
                    .getTargetCompanions(tag, companionId, pageSize, targetUserId, UserManager.get().getUserId());
        } else {
            call = ApiService.create(CompanionService.class)
                    .getCompanions(tag, companionId, pageSize, UserManager.get().getUserId());
        }
        LogUtil.d(" testsss33, targetUserId = "+targetUserId +" tag - "+tag);
        call.enqueue(new ApiResponseCallback<List<Companion>>() {
            @Override
            public void onResponse2(Call<ApiResponse<List<Companion>>> call, @NotNull List<Companion> response) {
                LogUtil.d(TAG, "loadData - response = " + GsonUtils.toJson(response));
                LogUtil.d(" testsss44, targetUserId = "+targetUserId +" tag - "+tag);
                CompanionRepository.getInstance().insertAll(response);
                onDataLoaded(response.size() > 0);
            }

            @Override
            public void onFailure2(Call<ApiResponse<List<Companion>>> call, String errorMsg) {
                LogUtil.d(" testsss55, targetUserId = "+targetUserId +" errorMsg - "+errorMsg);
                onDataLoaded(false);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void addBrowseCount(Companion companion) {
        ArchTaskExecutor.getIOThreadExecutor().execute(() -> CompanionRepository.getInstance().addBrowseCount(companion));
    }

    @SuppressWarnings("WeakerAccess")
    public void setTargetId(int targetUserId) {
        this.targetUserId = targetUserId;
    }
}
