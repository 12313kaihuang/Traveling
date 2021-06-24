package com.yu.hu.traveling.db.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.yu.hu.traveling.model.Companion;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/24 11:35
 **/
@Dao
public interface CompanionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Companion> companions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Companion companions);

    @SuppressWarnings("unused")
    @Query("delete from companion")
    void deleteAll();

    @Query("delete from companion where companionId = :id")
    void delete(int id);

    @Query("select * from companion where companionId = :companionId")
    LiveData<Companion> getCompanionLiveData(int companionId);

    /**
     * 获取游记和攻略
     */
    @Query("select * from companion order by createTime desc")
    DataSource.Factory<Integer, Companion> getFactory();

    @Query("select * from companion where authorId = :userId order by createTime desc")
    DataSource.Factory<Integer, Companion> getFactoryById(int userId);

}
