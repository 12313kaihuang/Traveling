package com.yu.hu.traveling.network;


import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Hy
 * created on 2020/04/29 16:23
 **/
@SuppressWarnings("unused")
public abstract class ApiResponseCallback<T> implements Callback<ApiResponse<T>> {

    private static final String TAG = "ApiResponseCallback";

    @Override
    public void onResponse(@NotNull Call<ApiResponse<T>> call, @NotNull Response<ApiResponse<T>> response) {
        ApiResponse<T> body = response.body();
        if (body == null || body.code != ApiResponse.CODE_SUCCESS) {
            onFailure2(call, body == null ? "body = null" : body.msg);
            return;
        }
        if (body.data == null) {
            onFailure2(call, "data == null");
            return;
        }
        onResponse2(call, body.data);
    }

    public abstract void onResponse2(Call<ApiResponse<T>> call,@NotNull T response);

    @Override
    public void onFailure(@NotNull Call<ApiResponse<T>> call, @NotNull Throwable t) {
        LogUtil.d(TAG, t.getMessage());
        onFailure2(call, t.getMessage());
    }

    public void onFailure2(Call<ApiResponse<T>> call, String errorMsg) {

    }
}
