package com.android.traveling.entity.msg;

/**
 * Created by HY
 * 2019/1/2 22:08
 *
 * 信息实体类
 *
 */
@SuppressWarnings("unused")
public class Msg {

    public final static int correctStatus = 0;
    public final static int errorStatus = 1;

    private int status;

    private String info;

    public Msg() {
    }

    public Msg(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public static Msg correctMsg(String info) {
        return new Msg(correctStatus, info);
    }

    public static Msg errorMsg(String info) {
        return new Msg(errorStatus, info);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
