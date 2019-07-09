package com.yu.hu.library.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 文件名：GsonUtil
 * 创建者：HY
 * 创建时间：2019/6/21 11:39
 * 描述：  GsonUtil
 */
@SuppressWarnings("unused")
public class GsonUtil {

    private static Gson mGson = new Gson();


    /**
     * toJson
     */
    public static String toJson(Object obj) {
        return mGson.toJson(obj);
    }

    /**
     * toJson
     */
    public static String toJson(Object obj, Type typeOfSrc) {
        return mGson.toJson(obj, typeOfSrc);
    }

    /**
     * toJson
     */
    public static String toJson(JsonElement jsonElement) {
        return mGson.toJson(jsonElement);
    }


    /**
     * fromJson
     */
    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return mGson.fromJson(json, classOfT);
    }

    /**
     * fromJson
     */
    public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return mGson.fromJson(json, classOfT);
    }

    /**
     * 转换成List
     */
    public static <T> List<T> jsonToList(String json) {
        //List<String> list = GsonUtil.fromJson("[\"1\",\"2\"]");
        return mGson.fromJson(json, new TypeToken<List<T>>() {}.getType());
    }

}
