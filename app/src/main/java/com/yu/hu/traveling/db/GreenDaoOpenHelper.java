package com.yu.hu.traveling.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * 项目名：Traveling
 * 包名：  com.yu.hu.traveling.db
 * 文件名：GreenDaoOpenHelper
 * 创建者：HY
 * 创建时间：2019/6/12 10:19
 * 描述：  GreenDaoOpenHelper
 */
public class GreenDaoOpenHelper extends DaoMaster.OpenHelper {

    private static DaoSession mDaoSession;

    private GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    public static void initDatabase(Context context) {
        GreenDaoOpenHelper helper =
                new GreenDaoOpenHelper(context, "traveling1.db", null);
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}
