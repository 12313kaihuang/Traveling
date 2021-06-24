package com.yu.hu.common.net;

/**
 * create by hy on 2019/11/18 21:16
 * 参数生成（加密）
 */
@SuppressWarnings("unused")
public interface IParamsGenerator {

    /**
     * 添加属性
     */
    <T extends ParamsGenerator> T addParam(String key, Object o);

    /**
     * 将参数列表转化为普通字符串
     */
    String generate();


    /**
     * 将参数列表转化为字符串并加密
     */
    String encrypt();
}
