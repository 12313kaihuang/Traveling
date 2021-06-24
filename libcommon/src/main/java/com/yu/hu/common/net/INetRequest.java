package com.yu.hu.common.net;

/**
 * Created by Hy on 2019/11/18 13:42
 * <p>
 * 网络请求接口化，便于拓展
 **/
public interface INetRequest {

    String get(String url, String params);

    String post(String url, String params);

}
