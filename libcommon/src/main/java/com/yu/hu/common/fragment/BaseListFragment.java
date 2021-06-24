package com.yu.hu.common.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.GsonUtils;
import com.yu.hu.common.R;
import com.yu.hu.common.paging.AbsViewModel;
import com.yu.hu.common.utils.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @param <T>  列表加载的数据类型
 * @param <VM> {@link #mViewModel} 需要相应的ViewModel配合使用
 * @param <D>  DataBinding
 * @author Hy
 * created on 2020/04/17 10:36
 * 与{@link AbsViewModel}联合使用，实现直接网络数据加载的paging列表
 * <p>
 * 封装paging分页加载
 * 自定义布局中RecyclerView的id必须为{@code recycler_view},否则会报错。
 * 同时需要注意，自定义布局时，其他控件里面是否含有同名的id，如果有请复写{@link} 方法修改listFragment对应的RecyclerView Id
 **/
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseListFragment<T, VM extends AbsViewModel<?, T>, D extends ViewDataBinding>
        extends BaseFragment<D> {

    protected VM mViewModel;
    protected RecyclerView mRecyclerView;
    protected PagedListAdapter<T, ? extends RecyclerView.ViewHolder> mAdapter;

    /**
     * 构建Adapter
     * <p>
     * 这里的泛型就重写的时候去设置了
     */
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
        //recyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemDecoration itemDecoration = createItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }

        mViewModel = genericViewModel();
        mViewModel.init();
    }

    /**
     * 对应RecyclerView的id，当布局中存在重名时可复写此方法
     */
    @IdRes
    protected int getRecyclerViewId() {
        return R.id.recycler_view;
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        //触发页面初始化数据加载的逻辑
        mViewModel.getPageData().observe(getViewLifecycleOwner(), list ->{
            LogUtil.d("search", "searchResult2 = " + GsonUtils.toJson(list));
            submitList(list);
        });

        //监听分页时有无更多数据,以决定是否关闭上拉加载的动画
        //这里第一条数据显示时会触发一次
        mViewModel.getBoundaryPageData().observe(getViewLifecycleOwner(), this::finishRefresh);
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
            Class modelClaz = ((Class) argument).asSubclass(AbsViewModel.class);
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

    /**
     * 提交一个新的list
     */
    public void submitList(PagedList<T> result) {
        LogUtil.d("search submitList size = " + result.size() + ", " + GsonUtils.toJson(result));
        finishRefresh(result.size() > 0);
        //只有当新数据集合大于0 的时候，才调用adapter.submitList
        //否则可能会出现 页面----有数据----->被清空-----空布局
        if (result.size() > 0) {
            mAdapter.submitList(result);
        }
    }

    /**
     * 可在刷新完成时回调
     */
    public void finishRefresh(boolean hasData) {

    }


}
