package com.yu.hu.traveling.ui.capture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.ui.preview.CapturePreviewFragment;

/**
 * @author Hy
 * created on 2020/04/25 10:29
 **/
@SuppressWarnings("WeakerAccess")
public class CaptureViewModel extends AndroidViewModel {

    private String previewUrl;
    private boolean isVideo;
    public String btnText;

    private MutableLiveData<FileModel> fileModel;
    boolean fromPreview = false;

    public CaptureViewModel(@NonNull Application application) {
        super(application);
        this.btnText = application.getResources().getString(R.string.preview_ok);
        this.fileModel = new MutableLiveData<>();
    }

    //重置
    public void reset() {
        this.previewUrl = null;
        this.fromPreview = false;
        fileModel.postValue(null);
    }

    public void setFromPreview(boolean fromPreview) {
        this.fromPreview = fromPreview;
    }

    /**
     * 跳转到预览界面
     *
     * @param previewUrl 资源url
     * @param isVideo    是否是视频
     */
    public void toPreview(MainActivity activity, String previewUrl, boolean isVideo) {
        this.previewUrl = previewUrl;
        this.isVideo = isVideo;
        activity.navigate(CapturePreviewFragment.PAGE_URL, null, null);
    }

    public void setFile(String filePath, int width, int height) {
        fileModel.postValue(new FileModel(filePath, width, height));
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public MutableLiveData<FileModel> getFileModel() {
        return fileModel;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public String getBtnText() {
        return btnText;
    }

    public static class FileModel extends BaseObservable {
        public String filePath;
        public int fileWidth;
        public int fileHeight;

        public FileModel(String filePath, int fileWidth, int fileHeight) {
            this.filePath = filePath;
            this.fileWidth = fileWidth;
            this.fileHeight = fileHeight;
        }

    }
}
