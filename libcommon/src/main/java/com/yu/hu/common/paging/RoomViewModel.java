package com.yu.hu.common.paging;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.yu.hu.common.utils.LogUtil;

/**
 * @param <Key>   一般对应{@code Value}所对应实体类的主键，用于标识区分实体的
 * @param <Value> 实体类 对应{@link com.yu.hu.common.fragment.BaseListFragment} T 实体类
 * @author Hy
 * created on 2020/04/20 19:41
 **/
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class RoomViewModel<Key, Value> extends AndroidViewModel {

    protected Context mContext;
    protected PagedList.Config mConfig;  //用于配置paging信息  每页加载多少数据等
    protected LiveData<PagedList<Value>> mPageData;

    protected int pageSize = 10; //每页Item个数 默认10
    private int initialLoadSizeHint = 15; //初始加载数量，即第一次加载数据时需要加载的数量 默认15

    protected OnNetDataLoadListener dataLoadListener;

    public RoomViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 由Room数据库中获得DataSourceFactory
     * {@code DataSource.Factory<Integer, Note> getNotes();}
     * <p>
     * 示例：{@code eturn NoteRepository.getNoteFactory();}
     */
    public abstract DataSource.Factory<Key, Value> createDataSourceFactory();

    public LiveData<PagedList<Value>> getPageData() {
        if (mConfig == null) {
            mConfig = buildListConfig();
        }
        if (mPageData == null) {
            mPageData = buildPagedData();
        }
        return mPageData;
    }

    /**
     * 子类可以重写此方法并在其中执行网络数据请求操作
     * eg:{@code loadData(null)}
     */
    public abstract void refresh();

    /**
     * 子类可以重写此方法并在其中执行网络数据请求操作
     * 可用于数据加载
     */
    public abstract void loadData(Key key);

    public void onDataLoaded(boolean hasData) {
        if (dataLoadListener != null) {
            dataLoadListener.onNetDataLoaded(hasData);
        }
    }

    public void setDataLoadListener(OnNetDataLoadListener dataLoadListener) {
        this.dataLoadListener = dataLoadListener;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setInitialLoadSizeHint(int initialLoadSizeHint) {
        this.initialLoadSizeHint = initialLoadSizeHint;
    }

    /**
     * key 用来唯一标识T的 见DataSource.Factory中的描述
     * 子类可重写
     */
    protected Key getInitialLoadKey() {
        return null;
    }


    /**
     * 创建{@link #mConfig}分页信息
     * 子类可以重写
     */
    protected PagedList.Config buildListConfig() {
        return new PagedList.Config.Builder()
                .setPageSize(pageSize)  //每次加载数据的时需要加载的数量
                .setInitialLoadSizeHint(initialLoadSizeHint)  //初始加载数量，即第一次加载数据时需要加载的数量
                //.setEnablePlaceholders(false)  //占位符
                .setPrefetchDistance(5)  //距离屏幕底部还有多少条数据时加载下一页，理论上应为屏幕可见Item数的倍数
                .build();
    }

    /**
     * 将数据源信息封装成LiveData
     * 子类可重写
     */
    protected LiveData<PagedList<Value>> buildPagedData() {
        return new LivePagedListBuilder<>(createDataSourceFactory(), mConfig)
                .setInitialLoadKey(getInitialLoadKey())  //初始化数据时需要传递的参数
                //.setFetchExecutor()  设置线程池
                .setBoundaryCallback(createBoundaryCallback())  //设置一个callBack 它可以监听到PagedList数据加载的状态
                .build();
    }

    /**
     * 创建一个callBack 它可以监听到PagedList数据加载的状态
     * 子类可以重写
     */
    protected PagedList.BoundaryCallback<Value> createBoundaryCallback() {
        //PagedList数据被加载 情况的边界回调callback
        //但 不是每一次分页 都会回调这里，具体请看 ContiguousPagedList#mReceiver#onPageResult
        //deferBoundaryCallbacks
        return new PagedList.BoundaryCallback<Value>() {
            @Override
            public void onZeroItemsLoaded() {
                //新提交的PagedList中没有数据
                LogUtil.d(getClass().getSimpleName(), "onZeroItemsLoaded: ");
            }

            @Override
            public void onItemAtFrontLoaded(@NonNull Value itemAtFront) {
                //新提交的PagedList中第一条数据被加载到列表上
                LogUtil.d(getClass().getSimpleName(), "onItemAtFrontLoaded: ");
            }

            @Override
            public void onItemAtEndLoaded(@NonNull Value itemAtEnd) {
                //新提交的PagedList中最后一条数据被加载到列表上
                LogUtil.d(getClass().getSimpleName(), "onItemAtEndLoaded: ");
            }
        };
    }

    //网络数据加载结果的监听
    public interface OnNetDataLoadListener {
        void onNetDataLoaded(boolean hasData);
    }
}
