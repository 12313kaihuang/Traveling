package com.android.traveling.entity.companion;

import android.support.annotation.NonNull;

import com.android.traveling.entity.comment.Reply;
import com.android.traveling.entity.msg.CompanionMsg;
import com.android.traveling.entity.msg.CompanionReplyMsg;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.service.CompanionService;
import com.android.traveling.util.ReflectionUtil;
import com.android.traveling.util.UtilTools;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HY
 * 2019/2/25 18:46
 * <p>
 * 结伴信息详情
 */
@SuppressWarnings("unused")
public class Companion implements Serializable {

    private Integer id;

    private Integer userId;

    private String imgUrl;

    private String nickName;

    private String title;

    private String content;

    private Date createTime;

    private Date startTime;

    private Date endTime;

    private String target;

    private Integer views;

    public Companion() {
    }

    /**
     * 获取最新的结伴消息
     *
     * @param callback 回调接口
     */
    public static void getNewest(Callback callback) {
        CompanionService companionService = UtilTools.getRetrofit().create(CompanionService.class);
        Call<CompanionMsg> msgCall = companionService.getNewsCompanion();
        msgCall.enqueue(new retrofit2.Callback<CompanionMsg>() {
            @Override
            public void onResponse(@NonNull Call<CompanionMsg> call, @NonNull Response<CompanionMsg> response) {
                CompanionMsg msg = response.body();
                if (msg == null) {
                    callback.onFailure(Msg.ERROR_STATUS, "msg == null");
                } else {
                    List<Companion> companions = msg.getCompanions();
                    if (companions.size() == 0) {
                        callback.onFailure(Msg.NO_DATA, "no data");
                    } else {
                        callback.onSuccess(companions);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanionMsg> call, @NonNull Throwable t) {
                callback.onFailure(Msg.ERROR_STATUS, t.getMessage());
            }
        });
    }

    /**
     * 加载更多结伴信息
     *
     * @param lastId   当前所显示的最旧的一结伴信息的id
     * @param callback 10条结伴信息
     */
    public static void loadMore(int lastId, Callback callback) {
        CompanionService companionService = UtilTools.getRetrofit().create(CompanionService.class);
        Call<CompanionMsg> call = companionService.loadMoreCompanions(lastId);
        call.enqueue(new retrofit2.Callback<CompanionMsg>() {
            @Override
            public void onResponse(@NonNull Call<CompanionMsg> call, @NonNull Response<CompanionMsg> response) {
                CompanionMsg msg = response.body();
                if (msg == null) {
                    callback.onFailure(Msg.ERROR_STATUS, "msg == null");
                } else {
                    List<Companion> companions = msg.getCompanions();
                    if (companions.size() == 0) {
                        callback.onFailure(Msg.NO_DATA, "no data");
                    } else {
                        callback.onSuccess(companions);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanionMsg> call, @NonNull Throwable t) {
                callback.onFailure(Msg.ERROR_STATUS, t.getMessage());
            }
        });
    }

    /**
     * 增加浏览量
     *
     * @param companionId 结伴消息id
     * @param callback    回调接口
     */
    public static void addBrowsNum(int companionId, Callback2 callback) {
        CompanionService companionService = UtilTools.getRetrofit().create(CompanionService.class);
        Call<Msg> msgCall = companionService.addBrowseNum(companionId);
        msgCall.enqueue(new retrofit2.Callback<Msg>() {
            @Override
            public void onResponse(@NonNull Call<Msg> call, @NonNull Response<Msg> response) {
                Msg msg = response.body();
                if (msg == null) {
                    callback.onFailure("msg == null");
                } else {
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Msg> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    /**
     * * 通过结伴信息id来查找与之相关的所有评论
     *
     * @param companionId 结伴信息id
     * @param callback    回调接口
     */
    public static void getCompanionComments(int companionId, Callback3 callback) {
        CompanionService companionService = UtilTools.getRetrofit().create(CompanionService.class);
        Call<CompanionReplyMsg> call = companionService.getCompanionComments(companionId);
        call.enqueue(new retrofit2.Callback<CompanionReplyMsg>() {
            @Override
            public void onResponse(@NonNull Call<CompanionReplyMsg> call, @NonNull Response<CompanionReplyMsg> response) {
                CompanionReplyMsg msg = response.body();
                if (msg == null) {
                    callback.onFailure(Msg.ERROR_STATUS, "msg == null");
                } else {
                    List<Reply> replies = msg.getReplies();
                    if (replies.size() == 0) {
                        callback.onFailure(Msg.NO_DATA, "no data");
                    } else {
                        callback.onSuccess(replies);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanionReplyMsg> call, @NonNull Throwable t) {
                callback.onFailure(Msg.ERROR_STATUS, t.getMessage());
            }
        });
    }

    /**
     * 添加结伴信息
     *
     * @param baseCompanion baseCompanion
     * @param callback      callback
     */
    public static void add(BaseCompanion baseCompanion, Callback callback) {
        CompanionService companionService = UtilTools.getRetrofit().create(CompanionService.class);
        Call<CompanionMsg> call = companionService.addCompanion(ReflectionUtil.toMap(baseCompanion));
        call.enqueue(new retrofit2.Callback<CompanionMsg>() {
            @Override
            public void onResponse(@NonNull Call<CompanionMsg> call, @NonNull Response<CompanionMsg> response) {
                CompanionMsg msg = response.body();
                if (msg == null) {
                    callback.onFailure(Msg.ERROR_STATUS, "msg == null");
                } else {
                    List<Companion> companions = msg.getCompanions();
                    if (companions == null || companions.size() == 0) {
                        callback.onFailure(Msg.NO_DATA, "no data");
                    } else {
                        callback.onSuccess(companions);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanionMsg> call, @NonNull Throwable t) {
                callback.onFailure(Msg.ERROR_STATUS, t.getMessage());
            }
        });
    }

    /**
     * 回调接口
     */
    public interface Callback {

        void onSuccess(List<Companion> companions);

        void onFailure(@Msg.ErrorCode int errCode, String reason);
    }

    /**
     * 回调接口2
     */
    public interface Callback2 {

        void onSuccess();

        void onFailure(String reason);
    }

    /**
     * 回调接口3
     */
    public interface Callback3 {

        void onSuccess(List<Reply> replies);

        void onFailure(@Msg.ErrorCode int errCode, String reason);
    }


    @Override
    public String toString() {
        return "Companion{" +
                "id=" + id +
                ", userId=" + userId +
                ", imgUrl='" + imgUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", target='" + target + '\'' +
                ", views=" + views +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
}
