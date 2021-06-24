package com.yu.hu.traveling.ui.detail;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.common.fragment.BaseListFragment;
import com.yu.hu.common.paging.AbsViewModel;
import com.yu.hu.common.paging.SimpleItemKeyedDataSource;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.db.repository.CompanionRepository;
import com.yu.hu.traveling.db.repository.NoteRepository;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.model.User;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.CommentService;
import com.yu.hu.traveling.network.CompanionService;
import com.yu.hu.traveling.network.NoteService;
import com.yu.hu.traveling.utils.TravelingUserProvider;
import com.yu.hu.traveling.utils.UserManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/18 22:10
 **/
public class CommentViewModel extends AbsViewModel<Integer, Comment> {

    //评论
    private int itemId;

    private int type; //评论类型
    private static final String TAG = "CommentViewModel";

    @SuppressWarnings("WeakerAccess")
    public CommentViewModel(@NonNull Application application, int itemId, int type) {
        super(application);
        this.itemId = itemId;
        this.type = type;
    }

    @Override
    public DataSource<Integer, Comment> createDataSource() {
        return new CommentDataSource();
    }

    //用于刷新界面列表的
    @SuppressWarnings("WeakerAccess")
    @SuppressLint("RestrictedApi")
    public PagedList<Comment> buildNewPagedList(List<Comment> initialList) {
        return new PagedList.Builder<>(((CommentDataSource) mDataSource).setInitialList(initialList), buildListConfig())
                .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                .build();
    }

    //发表评论
    @SuppressWarnings("unchecked")
    public void publishComment(BaseListFragment fragment, PagedList<Comment> currentList, int commentType, String content) {
        ApiService.create(CommentService.class)
                .addComment(itemId, UserManager.get().getUserId(), commentType, content)
                .enqueue(new ApiResponseCallback<Comment>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Comment>> call, @NotNull Comment response) {
                        ToastUtils.showShort("评论成功");
                        List<Comment> newList = new ArrayList<>();
                        newList.add(response);
                        if (currentList != null && currentList.size() > 0) {
                            newList.addAll(currentList);
                        }
                        //调用fragment.submit 而不是 adapter.submit是因为当从0评论到有评论时，emptyView需要隐藏掉
                        fragment.submitList(buildNewPagedList(newList));
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Comment>> call, String errorMsg) {
                        ToastUtils.showShort("评论失败");
                    }
                });
    }

    //发表回复
    public void publishReply(int commentType, String content, Comment comment) {
        ApiService.create(CommentService.class)
                .addComment(comment.id, UserManager.get().getUserId(), commentType, content)
                .enqueue(new ApiResponseCallback<Comment>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Comment>> call, @NotNull Comment response) {
                        ToastUtils.showShort("回复成功");
                        comment.addReplyCount();
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Comment>> call, String errorMsg) {
                        ToastUtils.showShort("评论失败");
                    }
                });
    }

    //评论详情页的发表回复操作
    @SuppressWarnings("unchecked")
    public void publishReply(BaseListFragment fragment, PagedList<Comment> currentList, int commentType, String content, int replyId) {
        ApiService.create(CommentService.class)
                .addReply(itemId, UserManager.get().getUserId(), commentType, content, replyId)
                .enqueue(new ApiResponseCallback<Comment>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Comment>> call, @NotNull Comment response) {
                        ToastUtils.showShort("评论成功");
                        List<Comment> newList = new ArrayList<>();
                        newList.add(response);
                        if (currentList != null && currentList.size() > 0) {
                            newList.addAll(currentList);
                        }
                        //调用fragment.submit 而不是 adapter.submit是因为当从0评论到有评论时，emptyView需要隐藏掉
                        fragment.submitList(buildNewPagedList(newList));
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Comment>> call, String errorMsg) {
                        ToastUtils.showShort("评论失败");
                    }
                });
    }

    /**
     * @param comment 删除评论
     */
    public void deleteComment(Context context, CommentAdapter adapter, Comment comment) {
        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.confirm_delete_comment))
                .setPositiveButton(context.getString(R.string.fragment_my_logout_ok), (dialog, which) -> {
                    dialog.dismiss();
                    deleteCommentInternal(adapter, comment);
                }).setNegativeButton(context.getString(R.string.fragment_my_logout_cancel), null)
                .create().show();
    }

    private void deleteCommentInternal(CommentAdapter adapter, Comment comment) {
        ApiService.create(CommentService.class)
                .deleteComment(comment.id)
                .enqueue(new ApiResponseCallback<Boolean>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Boolean>> call, @NotNull Boolean response) {
                        if (!response) {
                            ToastUtils.showShort("操作失败");
                            return;
                        }
                        List<Comment> newList = new ArrayList<>();
                        PagedList<Comment> currentList = adapter.getCurrentList();
                        if (currentList == null) {
                            adapter.submitList(buildNewPagedList(newList));
                            return;
                        }
                        for (Comment item : currentList) {
                            if (!item.equals(comment)) {
                                newList.add(item);
                            }
                        }
                        adapter.submitList(buildNewPagedList(newList));
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Boolean>> call, String errorMsg) {
                        ToastUtils.showShort("操作失败");
                    }
                });
    }


    /**
     * 删除游记
     */
    public void deleteNote(int noteId, MainActivity activity) {
        LogUtil.d("test - onDeleteIconClicked CommentViewModel");
        new AlertDialog.Builder(activity)
                .setMessage(mContext.getString(R.string.confirm_delete_note))
                .setPositiveButton(mContext.getString(R.string.fragment_my_logout_ok), (dialog, which) -> {
                    dialog.dismiss();
                    deleteNoteInternal(noteId, activity);
                }).setNegativeButton(mContext.getString(R.string.fragment_my_logout_cancel), null)
                .create().show();
    }

    /**
     * 删除结伴
     */
    public void deleteCompanion(int companionId, MainActivity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(mContext.getString(R.string.confirm_delete_note))
                .setPositiveButton(mContext.getString(R.string.fragment_my_logout_ok), (dialog, which) -> {
                    dialog.dismiss();
                    deleteCompanionInternal(companionId, activity);
                }).setNegativeButton(mContext.getString(R.string.fragment_my_logout_cancel), null)
                .create().show();
    }

    private void deleteNoteInternal(int noteId, MainActivity activity) {
        ApiService.create(NoteService.class)
                .delete(noteId)
                .enqueue(new ApiResponseCallback<Boolean>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Boolean>> call, @NotNull Boolean response) {
                        if (response) {
                            activity.popBackStack();
                            NoteRepository.getInstance().delete(noteId);
                            ToastUtils.showShort("删除成功");
                        } else {
                            onFailure2(call, "response = false");
                        }
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Boolean>> call, String errorMsg) {
                        LogUtil.d("deleteNote - " + errorMsg);
                        ToastUtils.showShort("删除失败");
                    }
                });
    }

    private void deleteCompanionInternal(int companionId, MainActivity activity) {
        ApiService.create(CompanionService.class)
                .delete(companionId)
                .enqueue(new ApiResponseCallback<Boolean>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Boolean>> call, @NotNull Boolean response) {
                        if (response) {
                            activity.popBackStack();
                            CompanionRepository.getInstance().delete(companionId);
                            ToastUtils.showShort("删除成功");
                        } else {
                            onFailure2(call, "response = false");
                        }
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Boolean>> call, String errorMsg) {
                        LogUtil.d("deleteNote - " + errorMsg);
                        ToastUtils.showShort("删除失败");
                    }
                });
    }

    public String buildDialogHint(User user) {
        if (user == null) {
            return mContext.getResources().getString(R.string.emoji_comment_hint);
        }
        return mContext.getResources().getString(R.string.reply_hint, user.name);
    }

    class CommentDataSource extends SimpleItemKeyedDataSource<Integer, Comment> {

        //用于增加/删除item
        private List<Comment> initialList;

        CommentDataSource setInitialList(List<Comment> initialList) {
            this.initialList = initialList;
            return this;
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Comment> callback) {
            if (initialList != null) {
                callback.onResult(initialList);
                return;
            }
            LogUtil.d(TAG, "loadInitial -Key =" + params.requestedInitialKey + ", size =" + params.requestedLoadSize);
            loadData(Integer.MAX_VALUE, params.requestedLoadSize, callback);
        }

        @Override
        public void loadData(Integer startId, int requestedLoadSize, LoadCallback<Comment> callback) {
            if (startId == null) startId = Integer.MAX_VALUE;
            Call<ApiResponse<List<Comment>>> call;
            if (type == Comment.TYPE_COMPANION) {
                call = ApiService.create(CompanionService.class).getComments(itemId, startId, pageSize, UserManager.get().getUserId());
            } else if (type == Comment.TYPE_NOTE) {
                call = ApiService.create(NoteService.class).getComments(itemId, startId, pageSize, UserManager.get().getUserId());
            } else {
                LogUtil.d(TAG, "type " + type + ",itemId-" + itemId + ", startId  " + startId);
                call = ApiService.create(CommentService.class).getComments(type, itemId, startId, pageSize, UserManager.get().getUserId());
            }
            ApiService.execute(call, new ApiService.ExecuteCallBack<List<Comment>>() {
                @Override
                public void onResponse(@Nullable List<Comment> response) {
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
        public Integer getKey(@NonNull Comment item) {
            return item.id;
        }
    }

    public void toChatPage(MainActivity activity, User user) {
        if (UserManager.isSelf(user.id)) {
            return;
        }
        try {
            // 点击联系人，直接跳转进入聊天界面
            Intent intent = new Intent(activity, LCIMConversationActivity.class);
            // 传入对方的 Id 即可
            LogUtil.d(TAG, "targetUserId - " + user.id);
            intent.putExtra(LCIMConstants.PEER_ID, String.valueOf(user.id));
            TravelingUserProvider.addUser(user);
            TravelingUserProvider.addUser(UserManager.get().getUser());
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "LeanCloud开启聊天异常");
        }
        //activity.navigate(TargetProfileFragment.PAGE_URL, TargetProfileFragment.createArgs(user));
    }
}
