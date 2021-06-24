package com.yu.hu.traveling.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.yu.hu.traveling.model.SearchHistory;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/19 21:43
 **/
@Dao
public interface SearchHistoryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHistory history);

    @Delete
    void delete(SearchHistory searchHistory);

    @Query("delete from searchhistory")
    void clearAll();

    /**
     * 查询最近{@code count}个搜索记录
     *
     * @param count 个数
     */
    @Query("select * from searchhistory order by searchTime desc limit :count")
    LiveData<List<SearchHistory>> getRecentSearchHistories(int count);

}
