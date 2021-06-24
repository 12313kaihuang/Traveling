package com.yu.hu.traveling.db.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.yu.hu.traveling.model.Note;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/20 15:25
 **/
@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Note> note);

    @Query("delete from note")
    void deleteAll();

    @Query("delete from note where noteId = :noteId")
    void delete(int noteId);

    @Query("select * from note where noteId = :noteId")
    LiveData<Note> getNoteLiveData(int noteId);

    /**
     * 获取游记和攻略
     */
    @Query("select * from note order by createTime desc")
    DataSource.Factory<Integer, Note> getFactory();

    /**
     * 只获取游记
     */
    @Query("select * from note where noteType = 1 order by createTime desc")
    DataSource.Factory<Integer, Note> getNotesFactory();

    @Query("select * from note where noteType = :noteType and authorId == :userId order by createTime desc")
    DataSource.Factory<Integer, Note> getFactoryById(int noteType, int userId);

    /**
     * 只获取攻略
     */
    @Query("select * from note where noteType = 2  order by createTime desc")
    DataSource.Factory<Integer, Note> getStrategiesFactory();

    /**
     * 模糊搜索
     */
    @Query("select * from note where title like :content or content like :content  order by createTime desc")
    List<Note> getAllFuzzily(String content);
}
