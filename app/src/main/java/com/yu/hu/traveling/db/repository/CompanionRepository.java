package com.yu.hu.traveling.db.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.yu.hu.traveling.db.TravelingDatabase;
import com.yu.hu.traveling.db.dao.CompanionDao;
import com.yu.hu.traveling.model.Companion;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/24 11:36
 **/
public class CompanionRepository {

    private static CompanionRepository sInstance;

    private CompanionDao companionDao;

    public static CompanionRepository getInstance() {
        if (sInstance == null) {
            synchronized (CompanionRepository.class) {
                if (sInstance == null) {
                    sInstance = new CompanionRepository();
                }
            }
        }
        return sInstance;
    }

    public CompanionRepository() {
        this.companionDao = TravelingDatabase.get().companionDao();
    }

    public DataSource.Factory<Integer, Companion> getFactory() {
        return companionDao.getFactory();
    }

    public void insertAll(List<Companion> data) {
        companionDao.insertAll(data);
    }

    public DataSource.Factory<Integer, Companion> getTargetCompanionFactory(int userId) {
        return companionDao.getFactoryById(userId);
    }

    public void addBrowseCount(Companion companion) {
        companion.ugc.browseCount++;
        companionDao.insert(companion);
    }

    public LiveData<Companion> getLiveCompanion(int companionId) {
        return companionDao.getCompanionLiveData(companionId);
    }

    public void insert(Companion companion) {
        companionDao.insert(companion);
    }

    public void delete(int companionId) {
        companionDao.delete(companionId);
    }
}
