package com.yu.hu.traveling.ui.companion;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.fragment.SimpleListFragment;
import com.yu.hu.traveling.model.Companion;
import com.yu.hu.traveling.ui.detail.CompanionDetailFragment;

/**
 * @author Hy
 * created on 2020/04/14 23:18
 * <p>
 * 结伴列表
 */
public class CompanionListFragment extends SimpleListFragment<Companion, CompanionViewModel> implements CompanionAdapter.OnCompanionItemClickListener {

    public static final String TAG_TYPE_SELF = "tag_type_self";

    private static final String KEY_TAG = "key_tag";

    private String tag;
    private int targetUserId;  //用于跳转他人的个人页面使用

    public static CompanionListFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString(KEY_TAG, tag);
        CompanionListFragment fragment = new CompanionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void beforeInitView() {
        super.beforeInitView();
        Bundle arguments = getArguments();
        if (arguments != null) {
            tag = arguments.getString(KEY_TAG, "");
        }
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        if (!TextUtils.isEmpty(tag) && TAG_TYPE_SELF.equals(tag)) {
            mDataBinding.refreshLayout.setEnableRefresh(false);
            mDataBinding.refreshLayout.setEnableLoadMore(false);
            mDataBinding.emptyView.setVisibility(View.VISIBLE);
            mDataBinding.emptyView.setBackgroundResource(R.color.white);
            mDataBinding.emptyView.setProgressBarVisibility(true);
            mViewModel.refresh();
        } else {
            mDataBinding.refreshLayout.autoRefresh();
        }
        mRecyclerView.setItemAnimator(null);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        if (!TextUtils.isEmpty(tag) && TAG_TYPE_SELF.equals(tag)) {
            mDataBinding.emptyView.setButton(null, null);
            mDataBinding.emptyView.setTitle(R.string.none_publish_hint);
        } else {
            mDataBinding.emptyView.setTitle(R.string.try_to_refresh);
            mDataBinding.emptyView.setButton(getString(R.string.reload), v -> mDataBinding.refreshLayout.autoRefresh());
        }
    }

    @Override
    protected CompanionViewModel genericViewModel() {
        CompanionViewModel companionViewModel = new ViewModelProvider(this, new CompanionViewModelFactory(tag))
                .get(CompanionViewModel.class);
        companionViewModel.setTargetId(targetUserId);
        return companionViewModel;
    }

    @Override
    public PagedListAdapter<Companion, ? extends RecyclerView.ViewHolder> getAdapter() {
        return new CompanionAdapter(this);
    }

    @Override
    public void onCompanionItemClicked(Companion companion) {
        Bundle args = CompanionDetailFragment.createArgs(companion);
        ((MainActivity) mActivity).navigate(CompanionDetailFragment.PAGE_URL, args);
        mViewModel.addBrowseCount(companion);
    }

    public CompanionListFragment setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
        return this;
    }
}
