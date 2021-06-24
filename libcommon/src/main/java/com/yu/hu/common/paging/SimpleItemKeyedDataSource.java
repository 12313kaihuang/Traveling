package com.yu.hu.common.paging;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.yu.hu.common.utils.LogUtil;

import java.util.Collections;

/**
 * @author Hy
 * created on 2020/04/30 14:42
 * <p>
 * ItemKeyedDataSource简单封装
 **/
public abstract class SimpleItemKeyedDataSource<Key, Value> extends ItemKeyedDataSource<Key, Value> {

    protected String TAG = getClass().getSimpleName();

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Value> callback) {
        LogUtil.d(TAG, "loadInitial -Key =" + params.requestedInitialKey + ", size =" + params.requestedLoadSize);
        loadData(params.requestedInitialKey, params.requestedLoadSize, callback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {
        LogUtil.d(TAG, "loadAfter - key = " + params.key + " size =" + params.requestedLoadSize);
        loadData(params.key, params.requestedLoadSize, callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {
        //向前加载这里就不需要了
        callback.onResult(Collections.emptyList());
    }

    /**
     * 执行网络数据请求,需要执行<strong>同步请求</strong>
     * 别忘了数据请求完成后调用{@code callback.onResult(Value);}来刷新列表
     *
     * @param key 这里可能会为null
     */
    public abstract void loadData(Key key, int requestedLoadSize, LoadCallback<Value> callback);
}
