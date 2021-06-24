package com.yu.hu.common.paging;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;


/**
 * @param <Key>   一般对应{@code Value}所对应实体类的主键，用于标识区分实体的
 * @param <Value> 实体类 对应{@link com.yu.hu.common.fragment.BaseListFragment} T 实体类
 * @author Hy
 * created on 2020/04/17 11:12
 * 与{@link }配合使用，关于paging的配置项可通过子类重写方法更改默认配置
 **/
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbsViewModel<Key, Value> extends AndroidViewModel {

    protected Context mContext;
    protected PagedList.Config mConfig;  //用于配置paging信息  每页加载多少数据等
    protected DataSource<Key, Value> mDataSource;
    protected LiveData<PagedList<Value>> mPageData;

    protected int pageSize = 10; //每页Item个数 默认10
    private int initialLoadSizeHint = 15; //初始加载数量，即第一次加载数据时需要加载的数量 默认15

    private MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

    /**
     * 创建数据源DataSource
     * <p>
     * 注意这里一定要new一个新的实例返回，
     * 不能创建一个实例然后返回此实例，否则{@link #invalidateData()}将失效
     */
    public abstract DataSource<Key, Value> createDataSource();

    public AbsViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    /**
     * 换移到这里来，通过手动调用来初始化paging配置信息，
     * 避免子类在构造函数中设置的参数无效
     */
    public void init() {
        //分页配置信息
        mConfig = buildListConfig();
        //构建pageData
        mPageData = buildPageData();
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
                //.setPrefetchDistance(5)  //距离屏幕底部还有多少条数据时加载下一页，理论上应为屏幕可见Item数的倍数
                .build();
    }

    /**
     * 将数据源信息封装成LiveData
     * 子类可重写
     */
    protected LiveData<PagedList<Value>> buildPageData() {
        return new LivePagedListBuilder<>(createDataSourceFactory(), mConfig)
                .setInitialLoadKey(getInitialLoadKey())  //初始化数据时需要传递的参数
                //.setFetchExecutor()  设置线程池
                .setBoundaryCallback(createBoundaryCallback())  //设置一个callBack 它可以监听到PagedList数据加载的状态
                .build();
    }

    /**
     * 用于构建DataSource
     * 子类可以重写
     * <p>
     * 使用Room Dao直接返回Factory的话，
     * 可以不用管{@link #createDataSource()}方法
     * 或者直接使用{@link RoomViewModel}
     *
     * @see RoomViewModel
     */
    protected DataSource.Factory<Key, Value> createDataSourceFactory() {
        return new DataSource.Factory<Key, Value>() {
            @Override
            public DataSource<Key, Value> create() {
                mDataSource = createDataSource();
                return mDataSource;
            }
        };
    }

    /**
     * key 用来唯一标识T的 见DataSource.Factory中的描述
     * 子类可重写
     */
    protected Key getInitialLoadKey() {
        return null;
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
                Log.d("AbsViewModel", "onZeroItemsLoaded: ");
                boundaryPageData.postValue(false);
            }

            @Override
            public void onItemAtFrontLoaded(@NonNull Value itemAtFront) {
                //新提交的PagedList中第一条数据被加载到列表上
                Log.d("AbsViewModel", "onItemAtFrontLoaded: ");
                boundaryPageData.postValue(true);
            }

            @Override
            public void onItemAtEndLoaded(@NonNull Value itemAtEnd) {
                //新提交的PagedList中最后一条数据被加载到列表上
                Log.d("AbsViewModel", "onItemAtEndLoaded: ");
            }
        };
    }

    /* **************************************************************** */

    /**
     * 调用{@code mDataSource.invalidate()}
     * 重新加载数据
     */
    public void invalidateData() {
        //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
        //详情见：LivePagedListBuilder#compute方法
        mDataSource.invalidate();
    }

    public LiveData<PagedList<Value>> getPageData() {
        return mPageData;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getInitialLoadSizeHint() {
        return initialLoadSizeHint;
    }

    public void setInitialLoadSizeHint(int initialLoadSizeHint) {
        this.initialLoadSizeHint = initialLoadSizeHint;
    }
}
