package com.android.traveling.entity.service;

import com.android.traveling.entity.msg.LoginMsg;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li
 * 文件名：UploadService
 * 创建者：HY
 * 创建时间：2019/3/6 13:49
 * 描述：  图片上传 retrofit接口
 */

public interface UploadService {


    /**
     * 上传头像
     *
     * @param part part
     * @return Msg
     */
    @Multipart
    @POST("uploadUserImg")
    Call<LoginMsg> uploadUserImg(@Query("userId") int userId, @Query("uploadType") int uploadType, @Part MultipartBody.Part part);

}
