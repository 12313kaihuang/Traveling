package com.yu.hu.traveling.ui.detail;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.blankj.utilcode.util.BarUtils;
import com.yu.hu.common.paging.AbsPagedListAdapter;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentNoteDetailMediaBinding;
import com.yu.hu.traveling.databinding.LayoutNoteDetailHeaderSingleVideoBinding;
import com.yu.hu.traveling.db.repository.NoteRepository;
import com.yu.hu.traveling.model.Note;

/**
 * @author Hy
 * created on 2020/04/27 16:37
 * <p>
 * 单个视频的Note单独拿出来做一个Fragment
 **/
@FragmentDestination(pageUrl = "note/detail/single_media")
public class SingleMediaNoteDetailFragment extends AbsNoteDetailFragment<FragmentNoteDetailMediaBinding>  {

    public static final String PAGE_URL = "note/detail/single_media";

    @SuppressWarnings("FieldCanBeLocal")

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);

        //全屏模式加一个状态栏的高度的margin
        int statusBarHeight = BarUtils.getStatusBarHeight();
        CoordinatorLayout.LayoutParams authorParams = (CoordinatorLayout.LayoutParams) mDataBinding.fullscreenAuthorInfo.getRoot().getLayoutParams();
        authorParams.topMargin += statusBarHeight;
        mDataBinding.fullscreenAuthorInfo.getRoot().setLayoutParams(authorParams);

        //include不能直接设置behavior  所以这里手动通过代码设置
        View authorLayout = mDataBinding.authorInfo.getRoot();
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) authorLayout.getLayoutParams();
        layoutParams.setBehavior(new ViewAnchorBehavior(R.id.media_view));

        addSingleVideoHeader(note);

        GridItem item = note.getImgItems().get(0);
        mDataBinding.mediaView.bindData(item.width, item.height, item.cover, item.url);

        //这里需要延迟一帧 等待布局完成，再来拿playerView的bottom值 和 coordinator的bottom值
        //做个比较。来校验是否进入详情页时时视频在全屏播放
        mDataBinding.mediaView.post(() -> {
            boolean fullScreen = mDataBinding.mediaView.getBottom() >= mDataBinding.coordinator.getBottom();
            LogUtil.d("mediaView Bottom" + mDataBinding.mediaView.getBottom()
                    + " - coordinatorBottom = " + mDataBinding.coordinator.getBottom());
            setViewAppearance(fullScreen);
        });

        mDataBinding.setNote(note);
        finishRefresh(false);
        mEmptyView.setProgressBarVisibility(true);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mDataBinding.actionClose.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());
        mDataBinding.interactionLayout.getRoot().setOnClickListener(v -> onCommentItemClicked(null));
        mDataBinding.interactionLayout.deleteLayout.setOnClickListener(v -> mViewModel.deleteNote(note.noteId, (MainActivity) mActivity));
        mDataBinding.authorInfo.getRoot().setOnClickListener(v -> toProfilePage());
        mDataBinding.fullscreenAuthorInfo.getRoot().setOnClickListener(v -> toProfilePage());

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mDataBinding.mediaView.getLayoutParams();
        ViewZoomBehavior behavior = (ViewZoomBehavior) layoutParams.getBehavior();
        //noinspection ConstantConditions
        behavior.setViewZoomCallback(height -> {
            //int bottom = mDataBinding.mediaView.getBottom();
            //moveUp 为true代表向上划 false是向下划
            //boolean moveUp = height < bottom;
            //boolean fullscreen = moveUp ? height >= mDataBinding.coordinator.getBottom() - mDataBinding.authorInfo.getRoot().getHeight()
            //       : height >= mDataBinding.coordinator.getBottom();
            boolean fullscreen = height >= mDataBinding.coordinator.getBottom() - mDataBinding.interactionLayout.getRoot().getHeight();
            setViewAppearance(fullscreen);
        });
    }

    private void toProfilePage() {
        mViewModel.toChatPage((MainActivity) mActivity, note.author);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mDataBinding.mediaView.inActive();
        } else {
            mDataBinding.mediaView.onActive();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_note_detail_media;
    }

    @Override
    public void onResume() {
        super.onResume();
        BarUtils.setStatusBarLightMode(mActivity, false);
        mDataBinding.mediaView.onActive();
    }

    @Override
    public void onPause() {
        super.onPause();
        BarUtils.setStatusBarLightMode(mActivity, true);
        mDataBinding.mediaView.inActive();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDataBinding.mediaView.release();
    }

    //设置全屏/非全屏样式
    private void setViewAppearance(boolean fullScreen) {
        mDataBinding.setFullScreen(fullScreen);
        mDataBinding.interactionLayout.setFullScreen(fullScreen);
        mDataBinding.fullscreenAuthorInfo.getRoot().setVisibility(fullScreen ? View.VISIBLE : View.GONE);

        //底部互动区域的高度
        int inputHeight = mDataBinding.interactionLayout.getRoot().getMeasuredHeight();
        //播放控制器的高度
        int ctrlViewHeight = mDataBinding.mediaView.getPlayController().getMeasuredHeight();
        //播放控制器的bottom值
        int bottom = mDataBinding.mediaView.getPlayController().getBottom();
        //全屏播放时，播放控制器需要处在底部互动区域的上面
        mDataBinding.mediaView.getPlayController().setY(fullScreen ? bottom - inputHeight - ctrlViewHeight
                : bottom - ctrlViewHeight);
        mDataBinding.interactionLayout.inputView.setBackgroundResource(fullScreen ? R.drawable.bg_edit_view2 : R.drawable.bg_edit_view);
    }

    //单个视频Header
    private void addSingleVideoHeader(@NonNull Note note) {
        LayoutNoteDetailHeaderSingleVideoBinding headerBinding
                = LayoutNoteDetailHeaderSingleVideoBinding.inflate(mLayoutInflater, mRecyclerView, false);
        headerBinding.setNote(note);

        NoteRepository.getInstance().getNoteLiveData(note.noteId)
                .observe(this, newNote -> {
                    if (newNote == null) {
                        LogUtil.d(" addMultiHeader note == null ");
                    }
                    if (newNote != null) {
                        LogUtil.d("focus - " + newNote.author.hasFollow);
                    }
                    mDataBinding.authorInfo.setNote(newNote);
                    mDataBinding.fullscreenAuthorInfo.setNote(newNote);
                    headerBinding.setNote(newNote);
                    mDataBinding.interactionLayout.setNote(newNote);
                });

        //添加HeaderView
        ((AbsPagedListAdapter) mAdapter).addHeaderView(headerBinding.getRoot());
    }
}
