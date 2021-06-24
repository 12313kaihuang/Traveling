package com.yu.hu.libnetwork;

import androidx.annotation.Nullable;

import java.io.IOException;

import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/14 15:17
 **/
@SuppressWarnings("unused")
public class ApiService {

    /**
     * 简化{@link retrofit2.Retrofit#create(Class)}方法
     */
    public static <T> T create(final Class<T> service) {
        return create(RetrofitManager.getsBaseUrl(), service);
    }

    public static <T> T create(String baseUrl, final Class<T> service) {
        return RetrofitManager.get(baseUrl).create(service);
    }

    //同步请求
    public static <T> void execute(Call<ApiResponse<T>> call, ExecuteCallBack<T> callBack) {
        try {
            ApiResponse<T> response = call.execute().body();
            if (response == null || response.code != ApiResponse.CODE_SUCCESS) {
                callBack.onFailure(response == null ? "response==null" : response.msg);
                return;
            }
            callBack.onResponse(response.data);
        } catch (IOException e) {
            e.printStackTrace();
            callBack.onFailure(e.getMessage());
        }
    }

    public interface ExecuteCallBack<T> {
        void onResponse(@Nullable T response);

        void onFailure(String msg);
    }
}
