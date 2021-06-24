package com.yu.hu.common.binding;

/**
 * @author Hy
 * created on 2020/04/17 17:20
 * <p>
 * 定义一种绑定数据的能力
 **/
public interface IBinding<T> {

    void bind(T data);
}
