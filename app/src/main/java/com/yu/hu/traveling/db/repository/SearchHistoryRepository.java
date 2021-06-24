package com.yu.hu.traveling.db.repository;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.yu.hu.traveling.db.TravelingDatabase;
import com.yu.hu.traveling.db.dao.SearchHistoryDao;
import com.yu.hu.traveling.model.SearchHistory;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/19 21:50
 **/
@SuppressWarnings("unused")
public class SearchHistoryRepository {

    private static volatile SearchHistoryRepository sINSTANCE;

    private SearchHistoryDao searchHistoryDao;

    public static SearchHistoryRepository getInstance() {
        if (sINSTANCE == null) {
            synchronized (SearchHistoryRepository.class) {
                if (sINSTANCE == null) {
                    sINSTANCE = new SearchHistoryRepository();
                }
            }
        }
        return sINSTANCE;
    }

    private SearchHistoryRepository() {
        searchHistoryDao = TravelingDatabase.get().searchHistoryDao();
    }

    public static LiveData<List<SearchHistory>> getRecentSearchHistories() {
        //默认显示前15条搜索记录
        return getInstance().getRecentSearchHistories(15);
    }

    @SuppressWarnings("WeakerAccess")
    public LiveData<List<SearchHistory>> getRecentSearchHistories(int count) {
        return searchHistoryDao.getRecentSearchHistories(count);
    }

    /**
     * 更新一条搜索历史
     */
    public void update(SearchHistory searchHistory) {
        if (TextUtils.isEmpty(searchHistory.searchContent)) {
            return;
        }
        searchHistory.searchTime = System.currentTimeMillis();
        searchHistoryDao.insert(searchHistory);
    }

    /**
     * 插入一条搜索历史
     */
    public void insert(SearchHistory searchHistory) {
        if (TextUtils.isEmpty(searchHistory.searchContent)) {
            return;
        }
        searchHistoryDao.insert(searchHistory);
    }

    /**
     * 删除一条搜索记录
     */
    public void delete(SearchHistory history) {
        searchHistoryDao.delete(history);
    }

    /**
     * 清空所有搜索记录
     */
    public void clearAll() {
        searchHistoryDao.clearAll();
    }
}
