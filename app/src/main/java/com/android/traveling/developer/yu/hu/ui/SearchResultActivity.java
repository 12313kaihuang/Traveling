package com.android.traveling.developer.yu.hu.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.traveling.R;
import com.android.traveling.developer.yu.hu.adaptor.SearchResultAdapter;
import com.android.traveling.entity.note.Note;
import com.android.traveling.util.RecyclerViewSpacesItemDecoration;
import com.android.traveling.util.UtilTools;
import com.android.traveling.util.XunfeiUtil;
import com.android.traveling.widget.SearchView;
import com.iflytek.cloud.SpeechError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.List;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.ui
 * 文件名：SearchResultActivity
 * 创建者：HY
 * 创建时间：2019/3/2 14:25
 * 描述：  显示查找游记结果
 */

public class SearchResultActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RECORD_AUDIO = 1;

    public static final String IS_NEED_VOICE = "isNeedVoice";

    SearchView searchView;
    ProgressBar progressBar;
    SmartRefreshLayout refreshLayout;

    RecyclerView recyclerView;
    List<Note> noteList;
    SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_note_result);
        initView();
        initData();

        //获取焦点并弹出软键盘
        searchView.getEt_input().requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(searchView.getEt_input(), 0);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        searchView = findViewById(R.id.search_view);
        progressBar = findViewById(R.id.progress_bar);
        refreshLayout = findViewById(R.id.refresh_layout);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(3));
        addEvents();
    }

    private void addEvents() {


        //语音输入
        searchView.setImageButtonVoiceClickListener((input, voice, view1) -> showSpeechDialog(input));
        searchView.setSearchIcon(getDrawable(R.drawable.ic_back2));
        searchView.setImageButtonSearchClickListener((input, search, view) -> finish());
        searchView.setonTextViewSearchClickListener((input, search, view) -> {
            String content = input.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            Note.searchHazily(content, new Note.Callback() {
                @Override
                public void onSuccess(List<Note> noteList) {
                    if (noteList.size() == 0) {
                        //没有找到
                        progressBar.setVisibility(View.INVISIBLE);
                        UtilTools.toast(SearchResultActivity.this, "没有找到相关文章，试试其他关键词吧");
                        return;
                    }
                    progressBar.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    SearchResultActivity.this.noteList = noteList;
                    createAdapter(input);
                    recyclerView.setAdapter(searchResultAdapter);
                }

                @Override
                public void onFailure(String reason) {
                    UtilTools.toast(SearchResultActivity.this, "加载失败" + reason);
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            String searchContent = searchView.getSearchContent();
            Note.searchMoreHazily(searchContent, noteList.get(noteList.size() - 1).getId(),
                    new Note.Callback() {
                        @Override
                        public void onSuccess(List<Note> noteList) {
                            if (noteList.size() == 0) {
                                UtilTools.toast(SearchResultActivity.this, "没有更多了");
                            } else {
                                SearchResultActivity.this.noteList.addAll(noteList);
                                searchResultAdapter.notifyDataSetChanged();
                            }
                            refreshLayout.finishLoadMore();
                        }

                        @Override
                        public void onFailure(String reason) {
                            UtilTools.toast(SearchResultActivity.this, "加载失败" + reason);
                        }
                    });
        });
    }

    /**
     * 创建适配器
     * @param input EditText
     */
    private void createAdapter(EditText input) {
        searchResultAdapter = new SearchResultAdapter(SearchResultActivity.this,
                input.getText().toString(), SearchResultActivity.this.noteList);
        //item点击事件
        searchResultAdapter.setOnItemClickListener((v, note, position) -> {
            Intent intent = new Intent(SearchResultActivity.this, NewsActivity.class);
            intent.putExtra(NewsActivity.POSITION, position);
            intent.putExtra(NewsActivity.s_NOTE, note);
            startActivity(intent);
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        String isNeedVoice = intent.getStringExtra(IS_NEED_VOICE);
        if (isNeedVoice != null) {
            showSpeechDialog(searchView.getEt_input());
        }
    }

    /**
     * 显示并进行语音输入
     *
     * @param input editText
     */
    private void showSpeechDialog(EditText input) {
        //申请录音权限
        //noinspection ConstantConditions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            XunfeiUtil.showSpeechDialog(this, new XunfeiUtil.onRecognizerResult() {
                @Override
                public void onSuccess(String result) {
                    input.setText(result);// 设置输入框的文本
                    input.requestFocus(); //请求获取焦点
                    input.setSelection(input.length());//把光标定位末尾
                }

                @Override
                public void onFaild(JSONException e) {
                    UtilTools.toast(SearchResultActivity.this, "onFaild e=" + e);
                }

                @Override
                public void onError(SpeechError speechError) {

                }
            });
        } else {
            //权限申请
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_RECORD_AUDIO);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意开启权限
                showSpeechDialog(searchView.getEt_input());
            } else {
                //不同意开启权限
                UtilTools.toast(this, "开启录制音频权限后才可进行语音输入！");
            }
        }
    }
}
