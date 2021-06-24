package com.yu.hu.libnetwork.utils;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.yu.hu.libnetwork.encrypt.IEncrypt;
import com.yu.hu.libnetwork.encrypt.RandomEncryptImpl;


/**
 * create by hy on 2019/11/18 22:33
 * <p>
 * 加密解密相关
 *
 * @see RandomEncryptImpl 基于Base64的随机加密/解密
 */
@SuppressWarnings("unused")
public class EncryptUtils {

    /**
     * 默认的encrypt
     */
    private static IEncrypt sDEFAULT_ENCRYPT = getRandomEncrypt();

    private static Gson sGson = new Gson();


    /**
     * 便于调用
     * 加密
     *
     * @see RandomEncryptImpl
     */
    public static String encrypt(String value) {
        return sDEFAULT_ENCRYPT.encrypt(value);
    }

    public static <T> String encrypt(T value) {
        return sDEFAULT_ENCRYPT.encrypt(sGson.toJson(value));
    }

    /**
     * 便于调用
     * 解密
     *
     * @see RandomEncryptImpl
     */
    @NonNull
    public static String decrypt(String value) {
        return sDEFAULT_ENCRYPT.decrypt(value);
    }


    /* ******************以下是单例类******************* */


    /**
     * 使用单例模式
     * <p>
     * 基于Base64加密
     *
     * @see #getRandomEncrypt()
     * @see RandomEncryptImpl
     */
    private static RandomEncryptImpl sINSTANCE_RANDOM;


    /**
     * 获取加密解密单例
     *
     * @see RandomEncryptImpl
     */
    @SuppressWarnings("WeakerAccess")
    public static IEncrypt getRandomEncrypt() {
        if (sINSTANCE_RANDOM == null) {
            synchronized (EncryptUtils.class) {
                if (sINSTANCE_RANDOM == null) {
                    sINSTANCE_RANDOM = new RandomEncryptImpl();
                }
            }
        }
        return sINSTANCE_RANDOM;
    }

}
