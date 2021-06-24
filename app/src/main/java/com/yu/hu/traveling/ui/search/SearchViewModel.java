package com.yu.hu.traveling.ui.search;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.DataSource;

import com.blankj.utilcode.util.ToastUtils;
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
 * created on 2020/04/20 20:23
 **/
public class SearchViewModel extends AbsViewModel<Integer, Note> {

    private String searchContent;

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void doSearch(String searchContent) {
        this.searchContent = searchContent;
        //重发DataSource的重新加载逻辑
        invalidateData();
    }

    @Override
    public DataSource<Integer, Note> createDataSource() {
        return new SearchDataSource();
    }

    @Override
    protected Integer getInitialLoadKey() {
        return -1;
    }

    class SearchDataSource extends SimpleItemKeyedDataSource<Integer, Note> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Note> callback) {
            LogUtil.d(TAG, "loadInitial -Key =" + params.requestedInitialKey + ", size =" + params.requestedLoadSize);
            //为了解决第二次搜索时  initialKey变成当前列表最后一个item的id
            loadData(Integer.MAX_VALUE, params.requestedLoadSize, callback);
        }

        @Override
        public void loadData(Integer noteId, int requestedLoadSize, LoadCallback<Note> callback) {
            if (TextUtils.isEmpty(searchContent)) {
                //每次进入searchFragment的时候就会触发但此时还未开始搜索，所以这时屏蔽掉该操作
                return;
            }
            if (noteId == null) noteId = Integer.MAX_VALUE;
            Call<ApiResponse<List<Note>>> call =
                    ApiService.create(NoteService.class).searchNotes(searchContent, noteId, requestedLoadSize, UserManager.get().getUserId());
            ApiService.execute(call, new ApiService.ExecuteCallBack<List<Note>>() {
                @Override
                public void onResponse(@Nullable List<Note> response) {
                    callback.onResult(response == null ? Collections.emptyList() : response);
                }

                @Override
                public void onFailure(String msg) {
                    LogUtil.d(TAG, msg);
                    callback.onResult(Collections.emptyList());
                    ToastUtils.showShort("搜索失败");
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
