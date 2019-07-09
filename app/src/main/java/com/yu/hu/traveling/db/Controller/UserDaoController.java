package com.yu.hu.traveling.db.Controller;

import com.yu.hu.library.util.LogUtil;
import com.yu.hu.library.util.RetrofitUtil;
import com.yu.hu.library.util.RxUtil;
import com.yu.hu.traveling.entity.user.User;
import com.yu.hu.traveling.db.GreenDaoOpenHelper;
import com.yu.hu.traveling.db.UserDao;
import com.yu.hu.traveling.entity.msg.LoginMsg;
import com.yu.hu.traveling.retrofit.UserService;
import com.yu.hu.traveling.rx.GlobalObserver;

import java.util.List;

import androidx.collection.LongSparseArray;
import rx.Observable;
import rx.functions.Func1;


/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.db.Controller
 * 文件名：UserDaoController
 * 创建者：HY
 * 创建时间：2019/6/25 11:51
 * 描述：  User相关数据库操作
 *
 * @see LongSparseArray
 */
public class UserDaoController extends BaseDaoController<User, Long> {

    /**
     * 最大缓存间隔 10s
     * 超过这个数则更新缓存
     */
    private static final long MAX_CACHE_INTERVAL = 10 * 1000;

    private static UserDaoController mInstance;

    private UserService mUserService;

    //用于存储用户上一次缓存的时间，避免段时间内多次缓存
    private LongSparseArray<Long> mCacheTimeMap;

    private UserDaoController(UserDao mDao) {
        super(mDao);
        mCacheTimeMap = new LongSparseArray<>();
        mUserService = RetrofitUtil.getInstance().getService(UserService.class);
    }

    public static UserDaoController getInstance() {
        if (mInstance == null) {
            synchronized (UserDaoController.class) {
                mInstance = new UserDaoController(GreenDaoOpenHelper.getDaoSession().getUserDao());
            }
        }
        return mInstance;
    }


    /**
     * 先从本地缓存中查找User，
     * 如果有就返回，没有就返回null
     * 不管有没有都会重新请求一次并更新缓存
     *
     * @param id userId
     * @see #findById(Long, boolean)
     */
    public synchronized User findById(Long id) {
        //是否之前缓存过
        if (mCacheTimeMap.containsKey(id)) {
            Long lastCacheTime = mCacheTimeMap.get(id);
            //是否超时
            //noinspection ConstantConditions
            if (System.currentTimeMillis() - lastCacheTime < MAX_CACHE_INTERVAL) {
                LogUtil.d("userId = " + id + " -- 不用更新缓存");
                return findById(id, false);
            }
        }
        //刷新一下缓存时间 避免短时间内再次请求会直接又去请求更新缓存
        mCacheTimeMap.put(id, System.currentTimeMillis());
        return findById(id, true);
    }

    /**
     * 先从本地缓存中查找User，
     * 如果有就返回，没有就返回null
     *
     * @param id            userId
     * @param isNeedRefresh 是否更新缓存
     */
    public User findById(Long id, boolean isNeedRefresh) {
        List<User> users = mDao.queryBuilder()
                .where(UserDao.Properties.Id.eq(id))
                .list();

        //刷新缓存
        if (isNeedRefresh) {
            refreshUserCache(id);
        }

        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    //更新用户缓存
    private void refreshUserCache(Long id) {
        mUserService.findUser(id)
                .compose(RxUtil.defaultSchedulers())
                .flatMap((Func1<LoginMsg, Observable<User>>) msg -> {
                    if (!msg.isStatusCorrect()) {
                        throw new UserCacheException(id, msg.getInfo());
                    }
                    if (msg.getUser() == null) {
                        throw new UserCacheException(id, "user == null");
                    }
                    return Observable.just(msg.getUser());
                })
                .subscribe(new GlobalObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        insertOrReplace(user);
                        mCacheTimeMap.put(user.getId(), System.currentTimeMillis());
                        LogUtil.d("更新用户缓存 user=" + user);
                    }
                });
    }

    public class UserCacheException extends RuntimeException {
        UserCacheException(Long id, String message) {
            super(id + "--" + message);
        }
    }

}
