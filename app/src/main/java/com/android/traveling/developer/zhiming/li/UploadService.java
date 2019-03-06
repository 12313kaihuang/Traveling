package com.android.traveling.developer.zhiming.li;

import com.android.traveling.entity.msg.Msg;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li
 * 文件名：UploadService
 * 创建者：HY
 * 创建时间：2019/3/6 13:49
 * 描述：  图片上传 retrofit
 */

public interface UploadService {

    /**
     * 上传头像
     * @param partList
     * @return
     */
    @Multipart
    @POST("uploadUserImg")
    Call<Msg> uploadUserImg(@Part List<MultipartBody.Part> partList);

}
