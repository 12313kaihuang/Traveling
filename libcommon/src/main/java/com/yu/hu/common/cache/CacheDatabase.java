package com.yu.hu.common.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.blankj.utilcode.util.Utils;

/**
 * @author Hy
 * created on 2020/04/15 17:26
 **/
@Database(entities = {Cache.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class CacheDatabase extends RoomDatabase {

    private static final CacheDatabase sDatabase;

    static {
        sDatabase = Room.databaseBuilder(Utils.getApp(), CacheDatabase.class, "simple_cache")
                .allowMainThreadQueries()
                //数据库创建和打开后的回调
                //.addCallback()
                //设置查询的线程池
                //.setQueryExecutor()
                //.openHelperFactory()
                //room的日志模式
                //.setJournalMode()
                //数据库升级异常之后的回滚
                //.fallbackToDestructiveMigration()
                //数据库升级异常后根据指定版本进行回滚
                //.fallbackToDestructiveMigrationFrom()
                // .addMigrations(CacheDatabase.sMigration)
                .build();
    }

    public abstract CacheDao getCache();

    public static CacheDatabase get() {
        return sDatabase;
    }
}
