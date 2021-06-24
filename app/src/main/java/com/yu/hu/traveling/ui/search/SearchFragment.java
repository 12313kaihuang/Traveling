package com.yu.hu.traveling.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.common.fragment.BaseListFragment;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.common.view.EmptyView;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentSearchBinding;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.ui.detail.AbsNoteDetailFragment;
import com.yu.hu.traveling.ui.detail.NoteDetailFragment;
import com.yu.hu.traveling.ui.detail.SingleMediaNoteDetailFragment;
import com.yu.hu.traveling.ui.home.OnNoteItemClickListener;

import org.jetbrains.annotations.NotNull;

/**
 * @author Hy
 * created on 2020/04/16 15:32
 **/
@FragmentDestination(pageUrl = "search")
public class SearchFragment extends BaseListFragment<Note, SearchViewModel, FragmentSearchBinding> implements OnNoteItemClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "SearchFragment";

    public static final String PAGE_URL = "search";

    private boolean isSearching = false;

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mDataBinding.searchHeader.searchBtn.setTextColor(getResources().getColor(R.color.color_text_normal));
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        ((SearchResultAdapter) mAdapter).setonNoteItemClickListener(this);

        mDataBinding.searchHeader.backBtn.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());
        mDataBinding.searchHeader.searchEditText.addTextChangedListener(getWatcher());
        mDataBinding.searchHeader.searchBtn.setOnClickListener(v -> onSearchBtnClicked());
        //软键盘搜索按钮监听
        mDataBinding.searchHeader.searchEditText.setOnEditorActionListener(createEditorActionListener());

        mDataBinding.searchHistoryView.setAutoRefreshObserver(this);
        mDataBinding.searchHistoryView.setOnItemClickListener(searchContent -> {
            mDataBinding.searchHeader.searchEditText.setText(searchContent);
            mDataBinding.searchHeader.searchEditText.setSelection(searchContent.length());
            onSearchBtnClicked();
            return true;
        });
    }

    @Override
    public void onItemClicked(Note note) {
        if (note.getDetailShowType() == AbsNoteDetailFragment.SHOW_TYPE_SINGLE_VIDEO) {
            ((MainActivity) mActivity).navigate(SingleMediaNoteDetailFragment.PAGE_URL, SingleMediaNoteDetailFragment.createArgs(note));
        } else {
            ((MainActivity) mActivity).navigate(NoteDetailFragment.PAGE_URL, NoteDetailFragment.createArgs(note));
        }
    }

    @Override
    public void finishRefresh(boolean hasData) {
        if (!isSearching) {
            return;
        }
        LogUtil.d("finishRefresh  hasData - " + hasData);
        isSearching = false;
        EmptyView mEmptyView = mDataBinding.emptyView;
        if (hasData) {
            mDataBinding.recyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setEmptyViewVisibility(true);
            mEmptyView.setTitle(mActivity.getString(R.string.no_data_searched));
        }
    }

    @Override
    protected RecyclerView.ItemDecoration createItemDecoration() {
        //默认给列表中的Item 一个 10dp的ItemDecoration
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        //noinspection ConstantConditions
        itemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.list_divider_2));
        return itemDecoration;
    }

    @Override
    public PagedListAdapter<Note, SearchResultAdapter.ViewHolder> getAdapter() {
        return new SearchResultAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    /**
     * @param input 搜索关键词
     */
    private void doSearch(CharSequence input) {
        isSearching = true;
        LogUtil.d("doSearch1 " + input.toString());
        ((SearchResultAdapter) mAdapter).setSearchContent(input.toString());
        mDataBinding.searchHistoryView.setVisibility(View.GONE);
        mDataBinding.emptyView.setProgressBarVisibility(true);
        mDataBinding.emptyView.setVisibility(View.VISIBLE);
        mViewModel.doSearch(input.toString());
    }

    /**
     * 搜索点击事件
     */
    private void onSearchBtnClicked() {
        CharSequence input = mDataBinding.searchHeader.searchEditText.getText();
        if (TextUtils.isEmpty(input)) {
            ToastUtils.showShort(R.string.input_search_content);
            return;
        }
        KeyboardUtils.hideSoftInput(mDataBinding.searchHeader.searchEditText);
        mDataBinding.searchHistoryView.setVisibility(View.GONE);
        mDataBinding.searchHistoryView.addHistory(input.toString());
        doSearch(input);
    }

    @NotNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mDataBinding.searchHeader.searchBtn.setTextColor(getResources().getColor(R.color.color_000));
                } else {
                    mDataBinding.searchHeader.searchBtn.setTextColor(getResources().getColor(R.color.color_text_normal));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @NotNull
    private TextView.OnEditorActionListener createEditorActionListener() {
        return (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchBtnClicked();
                return true;
            }
            return false;
        };
    }
}
