package com.yu.hu.common.exception;

/**
 * Created by Hy on 2019/11/29 18:56
 *
 * Exception基类
 **/
@SuppressWarnings("WeakerAccess")
public abstract class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException(String msg){
        super(msg);
    }

}
