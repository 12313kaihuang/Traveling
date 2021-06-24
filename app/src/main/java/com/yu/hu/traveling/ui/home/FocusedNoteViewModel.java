package com.yu.hu.traveling.ui.home;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.DataSource;

import com.yu.hu.common.paging.AbsViewModel;
import com.yu.hu.common.paging.SimpleItemKeyedDataSource;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.network.NoteService;
import com.yu.hu.traveling.utils.UserManager;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/30 16:38
 **/
public class FocusedNoteViewModel extends AbsViewModel<Integer, Note> {

    private String tag;

    @SuppressWarnings("WeakerAccess")
    public FocusedNoteViewModel(@NonNull Application application, String tag) {
        super(application);
        this.tag = tag;
    }

    @Override
    public DataSource<Integer, Note> createDataSource() {
        return new FocusDataSource();
    }

    @Override
    protected Integer getInitialLoadKey() {
        LogUtil.d("FocusDataSource getInitialLoadKey");
        return Integer.MAX_VALUE;
    }

    class FocusDataSource extends SimpleItemKeyedDataSource<Integer, Note> {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Note> callback) {
            LogUtil.d(TAG, "loadInitial -Key =" + params.requestedInitialKey + ", size =" + params.requestedLoadSize);
            loadData(getInitialLoadKey(), params.requestedLoadSize, callback);
        }

        @Override
        public void loadData(Integer noteId, int requestedLoadSize, LoadCallback<Note> callback) {
            if (UserManager.get().getUserId() <= 0) {
                LogUtil.d("FocusedNoteViewModel - 未登录");
                //未登录
                return;
            }
            if (noteId == null) noteId = Integer.MAX_VALUE;
            Call<ApiResponse<List<Note>>> call;
            call = ApiService.create(NoteService.class)
                    .getNotes(TextUtils.isEmpty(tag) ? NoteViewModel.TAG_FOCUS : tag, noteId, pageSize, UserManager.get().getUserId());
            LogUtil.d("loadData - getNotes -" + tag);
            ApiService.execute(call, new ApiService.ExecuteCallBack<List<Note>>() {
                @Override
                public void onResponse(@Nullable List<Note> response) {
                    callback.onResult(response == null ? Collections.emptyList() : response);
                }

                @Override
                public void onFailure(String msg) {
                    LogUtil.d(TAG, msg);
                    callback.onResult(Collections.emptyList());
                }
            });
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Note item) {
            return item.noteId;
        }
    }

}
