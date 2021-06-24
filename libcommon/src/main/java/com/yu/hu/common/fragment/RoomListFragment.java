package com.yu.hu.common.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.hu.common.R;
import com.yu.hu.common.paging.RoomViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Hy
 * created on 2020/04/20 19:41
 * <p>
 * 封装paging分页加载
 * <p>
 * 配合{@link RoomViewModel}使用实现数据库驱动，
 * 网络加载数据可在ViewModel中进行，获取到后直接插入数据库即可
 * <p>
 * 注意：
 * 自定义布局中RecyclerView的id必须为{@code recycler_view},否则会报错。
 * 同时需要注意，自定义布局时，其他控件里面是否含有同名的id，如果有请复写{@link} 方法修改listFragment对应的RecyclerView Id
 **/
@SuppressWarnings("WeakerAccess")
public abstract class RoomListFragment<T, VM extends RoomViewModel<?, T>, D extends ViewDataBinding>
        extends BaseFragment<D> implements RoomViewModel.OnNetDataLoadListener {

    protected VM mViewModel;
    protected RecyclerView mRecyclerView;
    protected PagedListAdapter<T, ? extends RecyclerView.ViewHolder> mAdapter;

    public abstract PagedListAdapter<T, ? extends RecyclerView.ViewHolder> getAdapter();

    @CallSuper
    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        mAdapter = getAdapter();
        mRecyclerView = mRootView.findViewById(getRecyclerViewId());
        if (mRecyclerView == null) {
            throw new RuntimeException("RecyclerView对应id需为R.id.recycler_view");
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration = createItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }

        mViewModel = genericViewModel();
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mViewModel.setDataLoadListener(this);
        //触发页面初始化数据加载的逻辑
        mViewModel.getPageData().observe(getViewLifecycleOwner(), this::submitList);
    }

    @Override
    public void onNetDataLoaded(boolean hasData) {
        finishRefresh(hasData);
    }

    public void submitList(PagedList<T> list) {
        mAdapter.submitList(list);
    }

    public void finishRefresh(boolean hasData) {

    }

    /**
     * 对应RecyclerView的id，当布局中存在重名时可复写此方法
     */
    @IdRes
    protected int getRecyclerViewId() {
        return R.id.recycler_view;
    }

    /**
     * 构建ViewModel
     * <p>
     * 子类可以重写此方法以生成复杂类型的ViewModel
     */
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    protected VM genericViewModel() {
        //mViewModel = ViewModelProviders.of(requireActivity(), getViewModelFactory()).get();

        //利用子类传递的泛型参数实例化出absViewModel 对象。
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] arguments = type.getActualTypeArguments();
        if (arguments.length > 1) {
            //获取到第二个泛型信息
            Type argument = arguments[1];
            Class modelClaz = ((Class) argument).asSubclass(RoomViewModel.class);
            return (VM) new ViewModelProvider(getViewModelOwner()).get(modelClaz);
        }
        throw new RuntimeException("AbsListFragment - genericViewModel() - 泛型类型有误");
    }

    /**
     * 用于设置创建ViewModel时传递{@code this}还是{@code mActivity}
     * 当用默认方法{@link #genericViewModel()}生成ViewModel时此方法才有效，
     * 自定义ViewModel生成的话不管此方法
     */
    protected ViewModelStoreOwner getViewModelOwner() {
        return this;
    }

    /**
     * ItemDecoration
     */
    protected RecyclerView.ItemDecoration createItemDecoration() {
        return null;
    }
}
