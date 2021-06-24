package com.yu.hu.common.utils;


import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.google.gson.reflect.TypeToken;
import com.yu.hu.common.entity.RequestResult;

import java.lang.reflect.Type;


/**
 * Created by Hy on 2019/11/18 11:52
 * <p>
 * 资源相关Util
 *
 * @see ResourceUtils
 **/
@SuppressWarnings("unused")
public class ResourceUtil {

    /**
     * 从asserts从读取模拟数据
     *
     * @see ResourceUtils#readAssets2List(String)
     */
    @SuppressWarnings("WeakerAccess")
    public static String readAssets2String(final String assetsFilePath) {
        return ResourceUtils.readAssets2String(assetsFilePath);
    }

    /**
     * 读取assets文件并转换成相应对象
     *
     * @param assetsFilePath 文件名
     * @param type           简单类型就传{@code BottomBar.class}
     *                       复杂类型通过{@code new TypeToken<RequestResult<T>>() {}.getType()}
     * @param <T>            泛型T
     */
    public static <T> T getFromAssets(final String assetsFilePath, final Type type) {
        try {
            String result = readAssets2String(assetsFilePath);
            return GsonUtils.fromJson(result, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param assetsFilePath assets文件名
     * @param <T>            response的类型
     * @see RequestResult#response
     */
    @Nullable
    public static <T> RequestResult<T> getRequestResult(final String assetsFilePath, Class<T> tClass) {
        try {
            String result = readAssets2String(assetsFilePath);
            return GsonUtils.fromJson(result, new TypeToken<RequestResult<T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
