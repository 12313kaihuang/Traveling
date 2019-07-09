package com.yu.hu.traveling.entity.exception;



/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.entity.exception
 * 文件名：MsgTransformException
 * 创建者：HY
 * 创建时间：2019/7/2 15:05
 * 描述：  当后台返回的消息状态码正确但所需要的对象为空时返回此异常
 */
public class NullResponseException extends TravelingRuntimeException{

    public NullResponseException(String message) {
        super(message);
    }
}
