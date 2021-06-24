package com.yu.hu.traveling.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.blankj.utilcode.util.GsonUtils;
import com.yu.hu.common.paging.RoomViewModel;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.db.repository.NoteRepository;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.NoteService;
import com.yu.hu.traveling.utils.UserManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/17 16:58
 **/
@SuppressWarnings("WeakerAccess")
public class NoteViewModel extends RoomViewModel<Integer, Note> {

    public static final String TAG_FOCUS = "focus";  //关注
    public static final String TAG_NEW = "new";  //最新
    public static final String TAG_NOTE = "note";  //游记
    public static final String TAG_STRATEGY = "strategy";  //攻略

    public static final String TAG_SELF_NOTE = "self_note";
    public static final String TAG_SELF_STRATEGY = "self_strategy";

    public static final String TAG_COLLECT = "tag_collect";  //收藏
    public static final String TAG_HISTORY = "tag_history";  //浏览历史

    private static final String TAG = "NoteViewModel";

    private String tag;
    private int targetId;//用于跳转他人的个人页面使用

    NoteViewModel(@NonNull Application application, String tag) {
        super(application);
        this.tag = tag;
        this.targetId = UserManager.get().getUserId();
        LogUtil.d("NoteViewModel - tag = " + tag);
    }

    @Override
    public DataSource.Factory<Integer, Note> createDataSourceFactory() {
        if (null == tag) {
            return NoteRepository.getDataSourceFactory();
        }
        switch (tag) {
            case TAG_NEW:
                return NoteRepository.getDataSourceFactory();
            case TAG_NOTE:
                return NoteRepository.getInstance().getNoteFactory();
            case TAG_STRATEGY:
                return NoteRepository.getInstance().getStrategyFactory();
            case TAG_SELF_NOTE:
                return NoteRepository.getInstance().getTargetNoteFactory(targetId);
            case TAG_SELF_STRATEGY:
                return NoteRepository.getInstance().getTargetStrategyFactory(targetId);
        }
        return NoteRepository.getDataSourceFactory();
    }

    @Override
    public void refresh() {
        LogUtil.d("NoteViewModel-refresh");
        loadData(Integer.MAX_VALUE);
    }

    @Override
    public void loadData(Integer noteId) {
        LogUtil.d(TAG, "loadData - startId = " + noteId);
        Call<ApiResponse<List<Note>>> call;
        if (targetId != 0
                && (NoteViewModel.TAG_SELF_NOTE.equals(tag) || NoteViewModel.TAG_SELF_STRATEGY.equals(tag))) {
            call = ApiService.create(NoteService.class)
                    .getTargetNotes(tag, noteId, pageSize, targetId, UserManager.get().getUserId());
        } else {
            call = ApiService.create(NoteService.class)
                    .getNotes(tag, noteId, pageSize, UserManager.get().getUserId());
        }
        LogUtil.d(TAG,"loadData "+call.request().toString());
        call.enqueue(new ApiResponseCallback<List<Note>>() {
            @Override
            public void onResponse2(Call<ApiResponse<List<Note>>> call, @NotNull List<Note> response) {
                LogUtil.d(TAG, "loadData - response = " + GsonUtils.toJson(response));
                NoteRepository.getInstance().insertAll(response);
                onDataLoaded(response.size() > 0);
            }

            @Override
            public void onFailure2(Call<ApiResponse<List<Note>>> call, String errorMsg) {
                onDataLoaded(false);
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
}
