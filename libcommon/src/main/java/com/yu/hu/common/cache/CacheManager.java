package com.yu.hu.common.cache;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.lang.reflect.Type;

/**
 * @author Hy
 * created on 2020/04/15 17:34
 **/
@SuppressWarnings("unused")
public class CacheManager {

    public static void delete(String key) {
        CacheDatabase.get().getCache().delete(key);
    }

    public static <T> void save(String key, T value) {
        Cache cache = new Cache();
        cache.key = key;
        cache.value = GsonUtils.toJson(value);
        CacheDatabase.get().getCache().save(cache);
    }

    public static String get(String key) {
        return CacheDatabase.get().getCache().get(key);
    }

    public static <T> T get(String key, Type type) {
        String value = get(key);
        return value == null ? null : GsonUtils.fromJson(value, type);
    }

    /**
     * 用于申请权限且仅申请一次
     */
    @SuppressLint("WrongConstant")
    public static void requestPermissionsOnlyOnce(String cacheKey, String[] permissions) {
        if (TextUtils.isEmpty(get(cacheKey))) {
            save(cacheKey, cacheKey);
            PermissionUtils.permission(permissions).request();
        }
    }
}
