package com.yu.hu.common.paging;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.yu.hu.common.utils.LogUtil;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/17 13:59
 * ItemKeyedDataSource的简单封装
 * <p>
 * loadxxx方法时执行在子线程中的,
 * 如果重写了loadxx方法记得要调用{@code callback.onResult(data)}
 **/
@SuppressWarnings("unused")
public abstract class AbsItemKeyedDataSource<Key, Value> extends ItemKeyedDataSource<Key, Value> {

    /**
     * 加载初始化数据时调用
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Value> callback) {
        LogUtil.d("loadInitial - size =" + params.requestedLoadSize);
        List<Value> data = loadData(null, params.requestedLoadSize);
        //通过这个方法将获取的数据集合添加进paging中
        callback.onResult(data);
    }

    /**
     * 需要向后加载分页数据时调用
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {
        LogUtil.d("loadAfter - key = " + params.key + "- size = " + params.requestedLoadSize);
        List<Value> data = loadData(params.key, params.requestedLoadSize);
        callback.onResult(data);
    }

    /**
     * 需要向前加载数据时调用
     */
    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {
        //callback.onResult(Collections.emptyList());
        LogUtil.d("loadBefore - key = " + params.key + "- size = " + params.requestedLoadSize);
    }

    public abstract List<Value> loadData(Key key, int requestedLoadSize);
}
