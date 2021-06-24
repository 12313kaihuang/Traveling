package com.yu.hu.traveling.db;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.blankj.utilcode.util.Utils;
import com.yu.hu.traveling.db.dao.CompanionDao;
import com.yu.hu.traveling.db.dao.NoteDao;
import com.yu.hu.traveling.db.dao.SearchHistoryDao;
import com.yu.hu.traveling.model.Companion;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.model.SearchHistory;
import com.yu.hu.traveling.utils.EntityConverter;

/**
 * @author Hy
 * created on 2020/04/16 11:49
 **/
@Database(entities = {Note.class, Companion.class, SearchHistory.class}, version = 1)
@TypeConverters({EntityConverter.class})
public abstract class TravelingDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    public abstract SearchHistoryDao searchHistoryDao();

    public abstract CompanionDao companionDao();

    //设置为单例，防止同时打开多个数据库实例
    private static volatile TravelingDatabase INSTANCE;

    public static TravelingDatabase get() {
        if (INSTANCE == null) {
            synchronized (TravelingDatabase.class) {
                if (INSTANCE == null) {
                    //创建一个对象TravelingDatabase并将其命名"traveling_database"
                    INSTANCE = Room.databaseBuilder(Utils.getApp().getApplicationContext(),
                            TravelingDatabase.class, "traveling_database")
                            .allowMainThreadQueries()  //允许在主线程进行查询操作
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
            }
        }
        return INSTANCE;
    }

}
