package com.yu.hu.traveling.utils;



import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.yu.hu.common.application.BaseApplication;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.utils.NavOptionsBuilder;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.model.Comment;
import com.yu.hu.traveling.model.Companion;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.model.Ugc;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.CommentService;
import com.yu.hu.traveling.network.CompanionService;
import com.yu.hu.traveling.network.NoteService;
import com.yu.hu.traveling.network.UserService;
import com.yu.hu.traveling.ui.login.LoginFragment;
import com.yu.hu.traveling.ui.share.ShareDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/19 11:51
 * <p>
 * 交互相关工具类
 **/
public class InteractionPresenter {

    private static final String TAG = "InteractionPresenter";

    /**
     * 打开分享面板
     */
    public static void openShare(Note note) {
        String shareContent = note.content;
        List<GridItem> imgItems = note.getImgItems();
        if (imgItems != null && imgItems.size() > 0) {
            shareContent = imgItems.get(0).cover;
        }
        openShare(shareContent, note);
    }

    /**
     * 分享结伴
     */
    public static void openShare(Companion companion) {
        String shareContent = companion.content;
        new ShareDialog.Builder()
                .setShareContent(shareContent)
                .show();
    }

    /**
     * 打开分享面板
     */
    public static void openShare(String shareContent, Note note) {
        new ShareDialog.Builder()
                .setShareContent(shareContent)
                .setShareItemClickListener(v -> shareInternal(note))
                .show();
    }

    private static void shareInternal(Note note) {
        ApiService.create(NoteService.class)
                .share(note.noteId)
                .enqueue(new ApiResponseCallback<Ugc>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Ugc>> call, @NotNull Ugc response) {
                        Note note1 = note.deepClone();
                        note1.ugc.shareCount = response.shareCount;
                        note1.update();
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Ugc>> call, String errorMsg) {
                        LogUtil.d(TAG, "分享失败 - " + errorMsg);
                    }
                });
    }

    /**
     * 给一个帖子点赞/取消点赞，它和给帖子点踩一踩是互斥的
     */
    public static void toggleNoteLike(Note note) {
        if (checkLogin()) {
            toggleLikeInternal(note);
        }
    }

    private static void toggleLikeInternal(Note note) {
        ApiService.create(NoteService.class)
                .toggleNoteLike(note.noteId, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Ugc>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Ugc>> call, @NotNull Ugc response) {
                        //这里有个坑 因为传进来的note是list中的note，所以这里修改数据（并插入数据库）后刷新列表
                        //会导致当前页面不会刷新，因为属性都是一样的，所以需要深拷贝一下
                        Note note1 = note.deepClone().toggleLike(response.hasLiked);
                        if (note1 != null) note1.update();
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Ugc>> call, String errorMsg) {
                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    //结伴评论 点赞
    public static void toggleCompanionLike(Companion companion) {
        if (!checkLogin()) {
            return;
        }
        ApiService.create(CompanionService.class)
                .toggleLike(companion.companionId, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Ugc>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Ugc>> call, @NotNull Ugc response) {
                        Companion result = companion.deepClone().toggleLike(response.hasLiked);
                        if (result != null) result.update();
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Ugc>> call, String errorMsg) {
                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    /**
     * 给一个帖子点踩一踩/取消踩一踩,它和给帖子点赞是互斥的
     */
    public static void toggleNoteDiss(Note note) {
        if (checkLogin()) {
            toggleDissInternal(note);
        }
    }

    private static void toggleDissInternal(Note note) {
        ApiService.create(NoteService.class)
                .toggleNoteDiss(note.noteId, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Ugc>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Ugc>> call, @NotNull Ugc response) {
                        //这里有个坑 因为传进来的note是list中的note，所以这里修改数据（并插入数据库）后刷新列表
                        //会导致当前页面不会刷新，因为属性都是一样的，所以需要深拷贝一下
                        Note note1 = note.deepClone().toggleDiss(response.hasDissed);
                        if (note1 != null) note1.update();
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Ugc>> call, String errorMsg) {
                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    /**
     * 收藏/取消收藏一个帖子
     */
    public static void toggleNoteCollect(Note note) {
        if (checkLogin()) {
            toggleCollectInternal(note);
        }
    }

    private static void toggleCollectInternal(Note note) {
        ApiService.create(NoteService.class)
                .toggleNoteCollect(note.noteId, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Ugc>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Ugc>> call, @NotNull Ugc response) {
                        Note note1 = note.deepClone();
                        note1.ugc.hasFavorite = response.hasFavorite;
                        note1.update();
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Ugc>> call, String errorMsg) {
                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    //收藏游记
    public static void toggleCompanionCollect(Companion companion) {
        if (!checkLogin()) {
            return;
        }
        ApiService.create(CompanionService.class)
                .toggleCollect(companion.companionId, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Ugc>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Ugc>> call, @NotNull Ugc response) {
                        LogUtil.d("toggleCompanionCollect - ugc = " + response.toString());
                        Companion companion1 = companion.deepClone();
                        companion1.ugc.hasFavorite = response.hasFavorite;
                        companion1.update();
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Ugc>> call, String errorMsg) {
                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    /**
     * 关注/取消关注
     */
    public static void toggleFocus(Note note) {
        if (!checkLogin() || UserManager.isSelf(note.authorId)) {
            return;
        }
        ApiService.create(UserService.class)
                .focus(note.authorId, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Boolean>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Boolean>> call, @NotNull Boolean response) {
                        note.deepClone().toggleFocus(response).update();
                    }
                });
    }

    /**
     * 关注/取消关注
     */
    public static void toggleFocus2(Companion companion) {
        if (!checkLogin() || UserManager.isSelf(companion.authorId)) {
            return;
        }
        ApiService.create(UserService.class)
                .focus(companion.authorId, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Boolean>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Boolean>> call, @NotNull Boolean response) {
                        companion.deepClone().toggleFocus(response).update();
                    }
                });
    }

    /**
     * 评论的点赞/取消
     */
    public static void toggleCommentLike(Comment comment) {
        if (checkLogin()) {
            toggleCommentLikeInternal(comment);
        }
    }

    private static void toggleCommentLikeInternal(Comment comment) {
        ApiService.create(CommentService.class)
                .toggleCommentLike(comment.id, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Ugc>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Ugc>> call, @NotNull Ugc response) {
                        comment.getUgc().toggleLiked(response.hasLiked);
                    }
                });
    }

    /**
     * 判断是否已登录  未登录则跳转登录页面
     *
     * @return 是否已登录
     */
    public static boolean checkLogin() {
        if (UserManager.get().isLogin()) {
            return true;
        }
        LogUtil.d("跳转登录");
        BaseApplication application = (BaseApplication) Utils.getApp();
        MainActivity topActivity = (MainActivity) application.getTopActivity();
        //noinspection ConstantConditions
        topActivity.navigate(AppConfig.getDestItemId(LoginFragment.PAGE_URL), NavOptionsBuilder.defaultSlideAnim());
        return false;
    }
}
