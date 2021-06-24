package com.yu.hu.traveling.ui.publish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.yu.hu.common.utils.DateUtils;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentPublishBinding;
import com.yu.hu.traveling.db.repository.CompanionRepository;
import com.yu.hu.traveling.db.repository.NoteRepository;
import com.yu.hu.traveling.fragment.BackInterceptFragment;
import com.yu.hu.traveling.model.Companion;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.ui.capture.CaptureViewModel;
import com.yu.hu.traveling.ui.preview.PreviewViewModel;
import com.yu.hu.traveling.utils.FileUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/15 13:24
 * <p>
 * 发布
 **/
@SuppressWarnings("WeakerAccess")
@FragmentDestination(pageUrl = "main/tabs/publish", needLogin = true, iconRes = R.drawable.icon_tab_publish)
public class PublishFragment extends BackInterceptFragment<FragmentPublishBinding>
        implements TextView.OnEditorActionListener, PublishViewModel.OnUploadCallback,
        TypeSelectDialog.OnTypeItemSelectedListener, PublishViewModel.OnNotePublishListener, PublishViewModel.OnCompanionPublishListener {

    public static final String TYPE_NOTE = "游记";
    public static final String TYPE_STRATEGY = "攻略";
    public static final String TYPE_COMPANION = "结伴";

    private static final String KEY_TYPE = "key_type";
    public static final int REQUEST_CODE_CHOOSE_PHOTO = 1001;
    public static final int REQUEST_CODE_CHOOSE_VIDEO = 1002;
    private static final int DEFAULT_FILE_COUNT = 20;

    @Target(ElementType.PARAMETER)
    @StringDef(value = {TYPE_NOTE, TYPE_STRATEGY, TYPE_COMPANION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {

    }

    private String mType;
    private String[] noteTypes = new String[]{TYPE_NOTE, TYPE_STRATEGY, TYPE_COMPANION};

    private CaptureViewModel captureViewModel;
    private PublishViewModel publishViewModel;
    private PreviewViewModel previewViewModel;

    @SuppressWarnings("unused")
    public static Bundle createArgs(@TYPE String type) {
        Bundle args = new Bundle();
        args.putString(KEY_TYPE, type);
        return args;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_publish;
    }

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mDataBinding.setNoteType(true);
        publishViewModel = new ViewModelProvider(this).get(PublishViewModel.class);
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        previewViewModel = new ViewModelProvider(mActivity).get(PreviewViewModel.class);
        captureViewModel = new ViewModelProvider(mActivity).get(CaptureViewModel.class);

        mDataBinding.titleView.setOnEditorActionListener(this);
        mDataBinding.contentView.setOnEditorActionListener(this);
        //退出
        mDataBinding.closeBtn.setOnClickListener(v -> handleOnBackPressed());
        //类型选择
        mDataBinding.actionAddTag.setOnClickListener(v ->
                publishViewModel.showTypeSelectDialog(getChildFragmentManager(), Arrays.asList(noteTypes), this));
        mDataBinding.publishBtn.setOnClickListener(v -> onPublishBtnClicked());
        mDataBinding.targetTime.setOnClickListener(v ->
                publishViewModel.showDatePickerDialog(getChildFragmentManager(), date -> mDataBinding.targetTime.setText(date)));

        mDataBinding.editGridView.setAddIconDrawableResource(R.drawable.ic_add_file);
        mDataBinding.editGridView.setOnItemClickListener((items, position) -> {
            if (position == items.size() - 1) {
                showAddFileDialog();
                return;
            }
            ToastUtils.showShort(items.get(position).coverPath);
        });

        captureViewModel.getFileModel().observe(this, fileModel -> {
            if (fileModel != null) {
                if (captureViewModel.isVideo()) {
                    FileUtils.generateVideoCover(fileModel.filePath)
                            .observe(this, coverPath -> mDataBinding.editGridView.addItem(GridItem.localItem(coverPath, fileModel.filePath)));
                } else {
                    mDataBinding.editGridView.addItem(GridItem.localItem(fileModel.filePath));
                }
                captureViewModel.reset();
            }
        });
    }

    //软键盘事件监听
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_NEXT:
                //下一个
                mDataBinding.contentView.requestFocus();
                return true;
            case EditorInfo.IME_ACTION_SEND:
                onPublishBtnClicked();
                return true;
        }
        return false;
    }

    private void onPublishBtnClicked() {
        if (TYPE_COMPANION.equals(mType)) {
            publishCompanion();
            return;
        }
        Editable content = mDataBinding.contentView.getText();
        if (content == null || !publishViewModel.checkInput(content.toString())
                || !publishViewModel.checkType(mType, noteTypes)) {
            return;
        }
        showLoadingDialog(getString(R.string.upload_file_hint));
        mLoadingDialog.setCancelable(false);
        List<GridItem> items = mDataBinding.editGridView.getItems();
        publishViewModel.uploadFile(this, items, this);
    }

    //发布结伴
    private void publishCompanion() {
        String title = mDataBinding.titleView.getEmojiText();
        String content = mDataBinding.contentView.getEmojiText();
        String target = mDataBinding.location.getEmojiText();
        @SuppressLint("SimpleDateFormat")
        long targetTime = DateUtils.string2Lone(mDataBinding.targetTime.getText().toString(), new SimpleDateFormat("yyyy-MM-dd"));
        if (!publishViewModel.checkCompanionInput(content, target, targetTime)) {
            return;
        }
        showLoadingDialog(getString(R.string.publish_hint));
        publishViewModel.publishCompanion(title, content, target, targetTime, this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onUploadSuccess(List<GridItem> gridItems) {
        LogUtil.d("PublishViewModel - uploadFile - items = " + GsonUtils.toJson(gridItems));
        mLoadingDialog.changeHint(getString(R.string.publish_hint));
        String title = mDataBinding.titleView.getText().toString();
        String content = mDataBinding.contentView.getText().toString();
        if (TYPE_COMPANION.equals(mType)) {
            String target = mDataBinding.location.getEmojiText();
            long targetTime = 2L;
            publishViewModel.publishCompanion(title, content, target, targetTime, this);
        } else {
            int type = TYPE_STRATEGY.equals(mType) ? Note.TYPE_STRATEGY : Note.TYPE_NOTE;
            publishViewModel.publishNote(title, content, type, gridItems, this);
        }
    }

    @Override
    public void onUploadFailure() {
        ToastUtils.showShort(getString(R.string.upload_file_failure));
        hideLoadingDialog();
    }

    @Override
    public void onPublishSuccess(Note note) {
        ToastUtils.showShort(R.string.publish_success);
        NoteRepository.getInstance().insert(note);
        hideLoadingDialog();
        previewViewModel.setNeedScrollToTop(true);
        ((MainActivity) mActivity).popBackStack();
    }

    @Override
    public void onPublishSuccess(Companion companion) {
        ToastUtils.showShort(R.string.publish_success);
        CompanionRepository.getInstance().insert(companion);
        hideLoadingDialog();
        ((MainActivity) mActivity).popBackStack();
    }

    @Override
    public void onPublishFailure(String reason) {
        ToastUtils.showShort(reason);
        hideLoadingDialog();
    }

    @Override
    public void handleOnBackPressed() {
        KeyboardUtils.hideSoftInput(mActivity);
        ((MainActivity) requireActivity()).popBackStack();
    }

    @Override
    public void onTypeItemSelected(String type) {
        mType = type;
        mDataBinding.setNoteType(!TYPE_COMPANION.equals(type));
        mDataBinding.actionAddTag.setText(type);
        mDataBinding.title.setText(type);
    }

    private void showAddFileDialog() {
        if (mDataBinding.editGridView.getItemsCount() >= DEFAULT_FILE_COUNT) {
            ToastUtils.showShort(getString(R.string.publish_max_img_hint, DEFAULT_FILE_COUNT));
            return;
        }
        publishViewModel.showChoosePhotoDialog(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
                String absolutePath = UriUtils.uri2File(data.getData()).getAbsolutePath();
                mDataBinding.editGridView.addItem(GridItem.localItem(absolutePath));
            } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_VIDEO) {
                String absolutePath = UriUtils.uri2File(data.getData()).getAbsolutePath();
                FileUtils.generateVideoCover(absolutePath).observe(this, coverUrl -> mDataBinding.editGridView.addItem(GridItem.localItem(coverUrl, absolutePath)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort(e.getMessage());
        }
    }
}
