package com.yu.hu.common.entity;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hy on 2019/11/18 11:44
 * <p>
 * 网络请求返回结果
 * 统一使用{@link SerializedName}注解，这样不用配置进proguard-rules.pro
 **/
@SuppressWarnings("unused")
@Keep
public class RequestResult<T> {

    @SerializedName("code")
    public int code;

    @SerializedName("msg")
    public String msg;

    @SerializedName("response")
    public T response;

}
