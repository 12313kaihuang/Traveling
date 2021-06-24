package com.yu.hu.common.utils;

/**
 * create by hy on 2019/11/18 22:55
 * <p>
 * 存放静态可配置常量
 * 例如{@link #BASE_URL}
 */
@SuppressWarnings({"unused", "JavadocReference", "WeakerAccess"})
public class Common {

    /**
     * 是否打印log
     */
    public static final boolean DEBUG_LOG = true;

    /**
     * base url
     */
    public static final String BASE_URL = "";

    /**
     * 加密前缀
     *
     * @see EncryptUtils.SimpleEncryptImpl
     */
    public static final String SIMPLE_ENCRYPT_PREFIX = "Tra";
}
