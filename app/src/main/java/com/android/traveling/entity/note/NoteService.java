package com.android.traveling.entity.note;

import com.android.traveling.entity.msg.NoteMsg;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.entity.note
 * 文件名：NoteService
 * 创建者：HY
 * 创建时间：2019/1/22 11:32
 * 描述：  retrofit接口
 */

public interface NoteService {

    /**
     * 获取最新的游记文章
     * @return 最新的篇文章
     */
    @GET("news")
    Call<NoteMsg> getNewest();

    @GET("loadMore")
    Call<NoteMsg> loadMore(@Query("lastId")int lastId);

}
