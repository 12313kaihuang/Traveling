package com.yu.hu.common.cache;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author Hy
 * created on 2020/04/15 17:24
 **/
@SuppressWarnings("UnusedReturnValue")
@Dao
public interface CacheDao {

    //增
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Cache cache);

    //删
    @Query("DELETE FROM cache WHERE `key`=:key")
    int delete(String key);

    //改
    @SuppressWarnings("unused")
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Cache cache);

    //查
    @Query("select value from cache where `key`=:key")
    String get(String key);
}
