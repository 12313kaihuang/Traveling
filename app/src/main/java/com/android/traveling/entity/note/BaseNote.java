package com.android.traveling.entity.note;


import android.support.annotation.NonNull;

import com.android.traveling.entity.msg.NoteMsg;
import com.android.traveling.entity.service.NoteService;
import com.android.traveling.util.DateUtil;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.google.gson.Gson;

import java.util.Date;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by HY
 * 2018/12/2 15:29
 * 游记表   游记的基本信息
 */

@SuppressWarnings("unused")
public class BaseNote {

    /**
     * 游记
     */
    public static final int TAG_1 = 1;

    /**
     * 攻略
     */
    public static final int TAG_2 = 2;


    //    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer tag = 1;

    private String createTime;


    public BaseNote() {
    }

    public BaseNote(Integer userId, String title, String content, Integer tag) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.createTime = DateUtil.transform(new Date());
    }


    /**
     * 发表游记/文章
     * @param part part
     * @param callBack callBack
     * @return Call<NoteMsg>
     */
    public Call<NoteMsg> publish(MultipartBody.Part part, CallBack callBack) {
        NoteService noteService = UtilTools.getRetrofit().create(NoteService.class);
        LogUtil.d("发表文章：" + (new Gson().toJson(this)));
        Call<NoteMsg> msgCall ;
        if (part != null) {
            msgCall = noteService.createNote(userId, tag, title, content, createTime, part);
        }else {
            msgCall = noteService.createNoteWithoutPicture(userId, tag, title, content, createTime);
        }
        msgCall.enqueue(new retrofit2.Callback<NoteMsg>() {
            @Override
            public void onResponse(@NonNull Call<NoteMsg> call, @NonNull Response<NoteMsg> response) {
                NoteMsg msg = response.body();
                if (msg != null) {
                    if (msg.isStatusCorrect()) {
                        callBack.onSuccess(msg.getNotes().get(0));
                    } else {
                        callBack.onFailure(msg.getInfo());
                    }
                } else {
                    callBack.onFailure("msg == null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NoteMsg> call, @NonNull Throwable t) {
                t.printStackTrace();
                callBack.onFailure(t.getMessage());
            }
        });
        return msgCall;
    }

    public interface CallBack {
        void onSuccess(Note note);

        void onFailure(String reason);
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
