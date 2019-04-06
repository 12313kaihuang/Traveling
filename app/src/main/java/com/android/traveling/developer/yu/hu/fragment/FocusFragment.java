package com.android.traveling.developer.yu.hu.fragment;


import com.android.traveling.entity.note.Note;
import com.android.traveling.util.UtilTools;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.yu.hu.fragment
 * 文件名：MyFragment
 * 创建者：HY
 * 创建时间：2018/9/22 13:59
 * 描述：  关注
 */

public class FocusFragment extends NewFragment {


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LogUtil.d("FocusFragment onCreate");
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        LogUtil.d("FocusFragment onCreate");
//        return inflater.inflate(R.layout.fragment_focus_on, container, false);
//    }


    @Override
    protected void LoadMore(RefreshLayout refreshLayout) {
        Note.loadMore(noteList.get(noteList.size() - 1).getId(), new Note.Callback() {
            @Override
            public void onSuccess(List<Note> noteList) {
                if (noteList.size() == 0) {
                    UtilTools.toast(getContext(), "没有更多文章了");
                    refreshLayout.finishLoadMore();
                    return;
                }
                FocusFragment.this.noteList.addAll(noteList);
                newsAdapter.notifyDataSetChanged();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onFailure(String reason) {
                UtilTools.toast(getContext(), "加载失败：" + reason);
                refreshLayout.finishLoadMore(false);
            }
        });
    }
}
