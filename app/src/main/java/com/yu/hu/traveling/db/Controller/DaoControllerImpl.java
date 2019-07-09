package com.yu.hu.traveling.db.Controller;

import java.util.List;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.db
 * 文件名：DaoControllerImpl
 * 创建者：HY
 * 创建时间：2019/6/25 11:31
 * 描述：  数据库表对应操作类的一些操作
 */
@SuppressWarnings("unused")
public interface DaoControllerImpl<T> {

    /**
     * 添加或替换
     */
    void insertOrReplace(T t);

    /**
     * 添加或替换
     */
    void insertOrReplace(List<T> tList);


    /**
     * 删除
     */
    void delete(T t);

    /**
     * 删除
     */
    void delete(List<T> tList);


    /**
     * 查找所有
     */
    List<T> queryAll();

}
