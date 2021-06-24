package com.yu.hu.traveling.ui.preview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.BarUtils;
import com.yu.hu.common.fragment.BaseFragment;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentMediaPreviewBinding;

/**
 * @author Hy
 * created on 2020/04/26 20:36
 * <p>
 * 多图片/视频 预览
 **/
@FragmentDestination(pageUrl = "preview/media")
public class MediaPreviewFragment extends BaseFragment<FragmentMediaPreviewBinding> {

    public static final String PAGE_URL = "preview/media";

    private static final String KEY_INIT_POSITION = "key_item";

    //不操作后多久后自动隐藏（当前第几张图片的提示）
    private static final int HIDE_INTERVAL = 1200;

    private int initPosition = 0;
    private PreviewViewModel mViewModel;

    public static Bundle createArgs(int position) {
        Bundle args = new Bundle();
        args.putInt(KEY_INIT_POSITION, position);
        return args;
    }

    @Override
    protected void beforeInitView() {
        super.beforeInitView();
        Bundle arguments = getArguments();
        if (arguments != null) {
            initPosition = arguments.getInt(KEY_INIT_POSITION, 0);
        }
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mViewModel = new ViewModelProvider(mActivity).get(PreviewViewModel.class);
        mDataBinding.viewPager.setAdapter(mViewModel.createAdapter(this));
        mDataBinding.viewPager.setCurrentItem(initPosition, false);
        if (mViewModel.needShowHint()) {
            setText(initPosition);
        }
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mDataBinding.closeBtn.setOnClickListener(v -> ((MainActivity) mActivity).popBackStack());
        mDataBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mDataBinding.textView.setVisibility(View.VISIBLE);
                setText(position);
            }
        });
    }

    private void setText(int position) {
        if (!mViewModel.needShowHint()) {
            return;
        }
        mDataBinding.textView.setVisibility(View.VISIBLE);
        String hint = mContext.getResources().getString(R.string.multi_preview_hint, position + 1, mViewModel.getItemSize());
        mDataBinding.textView.setText(hint);
        sHandler.removeCallbacksAndMessages(null);
        sHandler.sendEmptyMessageDelayed(0, HIDE_INTERVAL);
    }

    private Handler sHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            mDataBinding.textView.setVisibility(View.GONE);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        BarUtils.setStatusBarVisibility(mActivity, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        BarUtils.setStatusBarVisibility(mActivity, true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_media_preview;
    }
}
