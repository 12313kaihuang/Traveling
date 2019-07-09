package com.yu.hu.traveling.entity.msg;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by HY
 * 2019/1/2 22:08
 * <p>
 * 信息实体类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Msg {

    public final static int CORRECT_STATUS = 0;
    public final static int ERROR_STATUS = 1;
    public final static int NO_DATA = 2;

    //自定义注解
    @IntDef({ERROR_STATUS, NO_DATA})  //注解仅存在于源码中，在class字节码文件中不包含
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
    }

    private int status;

    private String info;

    public Msg() {
    }

    public Msg(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public static Msg correctMsg(String info) {
        return new Msg(CORRECT_STATUS, info);
    }

    public static Msg errorMsg(String info) {
        return new Msg(ERROR_STATUS, info);
    }

    /**
     * 状态是否正确
     *
     * @return boolean
     */
    public boolean isStatusCorrect() {
        return status == CORRECT_STATUS;
    }

    public int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "status=" + status +
                ", info='" + info + '\'' +
                '}';
    }
}
