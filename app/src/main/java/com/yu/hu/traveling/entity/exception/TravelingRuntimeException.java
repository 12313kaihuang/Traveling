package com.yu.hu.traveling.entity.exception;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.entity.exception
 * 文件名：TravelingRuntimeException
 * 创建者：HY
 * 创建时间：2019/7/3 9:22
 * 描述：  用于区分异常情况下抛出的运行时异常
 */
public class TravelingRuntimeException extends RuntimeException {

    public TravelingRuntimeException(String message) {
        super(message);
    }
}
