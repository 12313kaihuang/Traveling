package com.yu.hu.traveling.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yu.hu.library.activity.BaseActivity;
import com.yu.hu.library.util.SoftKeyboardUtils;
import com.yu.hu.library.widget.SimpleItemDecoration;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.adapter.SearchResultAdapter;
import com.yu.hu.traveling.entity.note.Note;
import com.yu.hu.traveling.mvp.SearchResultPresenter;
import com.yu.hu.traveling.mvp.impl.SearchResultPresence;
import com.yu.hu.traveling.widget.SearchView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 项目名：Traveling-New
 * 包名：  com.yu.hu.traveling.activity
 * 文件名：SearchResultActivity
 * 创建者：HY
 * 创建时间：2019/7/2 14:02
 * 描述：  搜索游记结果
 */
public class SearchResultActivity extends BaseActivity<SearchResultPresence, SearchResultPresenter>
        implements SearchResultPresence {

    private static final int REQUEST_CODE_RECORD_AUDIO = 1;

    public static final String IS_NEED_VOICE = "isNeedVoice";

    @BindView(R.id.search_view)
    SearchView mSearchView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    //游记对象列表
    List<Note> mNoteList;

    //adapter
    SearchResultAdapter mSearchResultAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_note_result;
    }

    @Override
    protected SearchResultPresenter attachPresenter() {
        return new SearchResultPresenter(this);
    }

    @Override
    protected void onPrepare(@Nullable Bundle savedInstanceState) {
        initView();

        //弹出软键盘
        mSearchView.requestFocus(this);
        SoftKeyboardUtils.showSoftKeyboard(this);
    }

    @Override
    public void onSearchResult(@NonNull List<Note> noteList) {
        if (noteList.size() == 0) {
            //没有找到
            mProgressBar.setVisibility(View.INVISIBLE);
            ToastUtils.showShort("没有找到相关文章，试试其他关键词吧");
            return;
        }
        mProgressBar.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.VISIBLE);
        mNoteList = noteList;
        createAdapter(mSearchView.getEt_input());
        mRecyclerView.setAdapter(mSearchResultAdapter);
    }

    @Override
    public void onSearchMoreResult(@NonNull List<Note> noteList) {
        mRefreshLayout.finishLoadMore();
        if (noteList.size() == 0) {
            ToastUtils.showShort("没有更多了");
            return;
        }
        mNoteList.addAll(noteList);
        mSearchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_RECORD_AUDIO) {
            //录音权限申请
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意开启权限
                showSpeechDialog(mSearchView.getEt_input());
            } else {
                //不同意开启权限
                ToastUtils.showShort("开启录制音频权限后才可进行语音输入！");
            }
        }
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(3));

        //判断是否直接语音输入
        Intent intent = getIntent();
        String isNeedVoice = intent.getStringExtra(IS_NEED_VOICE);
        if (isNeedVoice != null) {
            showSpeechDialog(mSearchView.getEt_input());
        }

        //语音输入
        mSearchView.setImageButtonVoiceClickListener((input, voice, view1) -> showSpeechDialog(input));
        mSearchView.setImageButtonSearchClickListener((input, search, view) -> onBackPressed());
        mSearchView.setonTextViewSearchClickListener((input, search, view) -> {
            String content = input.getText().toString();
            mProgressBar.setVisibility(View.VISIBLE);
            //查询
            mPresenter.searchHazily(content);
        });

        //加载更多监听事件
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            String searchContent = mSearchView.getSearchContent();
           mPresenter.searchMoreHazily(searchContent,mNoteList.get(mNoteList.size() - 1).getId());
        });
    }

    /**
     * 创建适配器
     *
     * @param input EditText
     */
    private void createAdapter(EditText input) {
        mSearchResultAdapter = new SearchResultAdapter(SearchResultActivity.this,
                input.getText().toString(), mNoteList);
        //item点击事件
        mSearchResultAdapter.setOnItemClickListener((v, note, position) -> {
            //todo 跳转到详情页
            Intent intent = new Intent(SearchResultActivity.this, NoteDetailActivity.class);
            intent.putExtra(NoteDetailActivity.POSITION, position);
            intent.putExtra(NoteDetailActivity.s_NOTE, note);
            startActivity(intent);
        });
    }

    //讯飞语音输入
    private void showSpeechDialog(EditText input) {

        //申请录音权限
        //noinspection ConstantConditions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            //todo  讯飞与容联云冲突暂未解决
            ToastUtils.showShort("抱歉，该功能暂未实现，安装老版本可体验此功能");
        } else {
            //权限申请
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_RECORD_AUDIO);
            }
        }

    }
}
