package com.yu.hu.traveling.db.Controller;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.db
 * 文件名：BaseDaoController
 * 创建者：HY
 * 创建时间：2019/6/25 11:37
 * 描述：  DaoController基类
 *
 * @param <T> 实体类的类型
 * @param <K> 实体类主键的类型
 */
public abstract class BaseDaoController<T, K> implements DaoControllerImpl<T> {

    @SuppressWarnings("WeakerAccess")
    protected AbstractDao<T, K> mDao;

    @SuppressWarnings("WeakerAccess")
    public BaseDaoController(AbstractDao<T, K> mDao) {
        this.mDao = mDao;
    }

    public AbstractDao<T, K> getDao() {
        return mDao;
    }

    @Override
    public void insertOrReplace(T t) {
        mDao.insertOrReplace(t);
    }

    @Override
    public void insertOrReplace(List<T> ts) {
        mDao.insertOrReplaceInTx(ts);
    }

    @Override
    public void delete(T t) {
        mDao.delete(t);
    }

    @Override
    public void delete(List<T> ts) {
        mDao.deleteInTx(ts);
    }

    @Override
    public List<T> queryAll() {
        return mDao.queryBuilder().list();
    }

}
