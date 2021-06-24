package com.yu.hu.traveling.network;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.encrypt.IEncrypt;
import com.yu.hu.libnetwork.encrypt.RandomEncryptImpl;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Hy
 * created on 2020/04/28 21:30
 **/
@SuppressWarnings({"unused"})
public class EnqueueUtils {

    private static final String TAG = "EnqueueCallback";

    private static IEncrypt sEncrypt = new RandomEncryptImpl();

    /**
     * @param call     call
     * @param callback {@link Callback}
     * @param type     {@link ApiResponse<T>}的类型
     * @param <T>      最终解析成的实体类的类型
     */
    @SuppressLint("RestrictedApi")
    public static <T> void enqueue(Call<String> call, @NonNull Callback<T> callback, Type type) {
        ArchTaskExecutor.getIOThreadExecutor().execute(() -> call.enqueue(createCallback(callback, type)));
    }

    private static <T> retrofit2.Callback<String> createCallback(@NonNull Callback<T> callback, Type type) {
        return new retrofit2.Callback<String>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                String body = response.body();
                Log.i(TAG, "onResponse: body = " + body);
                if (TextUtils.isEmpty(body)) callback.onFailure("body = null");
                try {
                    //LogUtil.d("onResponse decrypt1 = " + body);
                    //String decrypt = DesEncryptUtil.decrypt(body);
                    //LogUtil.d("onResponse decrypt2 = " + decrypt);
                    ApiResponse<T> apiResponse = GsonUtils.fromJson(body, type);
                    if (apiResponse == null || apiResponse.code != ApiResponse.CODE_SUCCESS ||
                            apiResponse.data == null) {
                        callback.onFailure(apiResponse == null ? "apiResponse = null" : apiResponse.msg);
                    } else {
                        ArchTaskExecutor.getMainThreadExecutor().execute(() -> callback.onResponse(apiResponse.data));
                    }
                } catch (Exception e) {
                    onFailure(call, e);
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                t.printStackTrace();
                LogUtil.d(TAG, "onFailure" + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        };
    }

    public static abstract class SimpleCallback<T> implements Callback<T> {

        @Override
        public void onFailure(String msg) {
            ToastUtils.showShort(msg);
        }
    }

    public interface Callback<T> {
        void onResponse(T response);

        void onFailure(String msg);
    }



}
