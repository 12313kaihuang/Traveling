package com.yu.hu.traveling.ui.capture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.UseCase;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.VideoCaptureConfig;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.libnavannotation.FragmentDestination;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.FragmentCaptureBinding;
import com.yu.hu.traveling.fragment.BackInterceptFragment;
import com.yu.hu.traveling.view.RecordView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hy
 * created on 2020/04/24 23:31
 * <p>
 * cameraX 拍照/录像
 **/
@FragmentDestination(pageUrl = "capture")
public class CaptureFragment extends BackInterceptFragment<FragmentCaptureBinding> {

    public static final String PAGE_URL = "capture";
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};

    //后置摄像头
    private CameraX.LensFacing mLensFacing = CameraX.LensFacing.BACK;
    //旋转角度
    private int rotation = Surface.ROTATION_0;
    //分辨率
    private Size resolution = new Size(1280, 720);
    //宽高比
    private Rational rational = new Rational(9, 16);

    private Preview preview;
    private ImageCapture imageCapture; //用于拍照
    private VideoCapture videoCapture; //用于录像
    private boolean takingPicture;
    private String outputFilePath;

    private CaptureViewModel mViewModel;

    @Override
    protected void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        BarUtils.setStatusBarVisibility(mActivity, false);
        mViewModel = new ViewModelProvider(mActivity).get(CaptureViewModel.class);
        requestPermissions();
    }

    @Override
    protected void onInitEvents(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onInitEvents(view, savedInstanceState);
        mDataBinding.recordView.setOnRecordListener(createOnRecordListener());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mViewModel.fromPreview) {
            mViewModel.fromPreview = false;
            //当设备处于竖屏情况时，宽高的值 需要互换，横屏不需要
            mViewModel.setFile(outputFilePath, resolution.getHeight(), resolution.getWidth());
            handleOnBackPressed();
        }
    }

    @Override
    public void handleOnBackPressed() {
        BarUtils.setStatusBarVisibility(mActivity, true);
        ((MainActivity) mActivity).popBackStack();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_capture;
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        PermissionUtils.permission(PERMISSIONS)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        initCameraX();
                    }

                    @Override
                    public void onDenied() {
                        ((MainActivity) mActivity).popBackStack();
                    }
                })
                .request();
    }

    /**
     * {@link RecordView}事件监听
     */
    @NotNull
    private RecordView.OnRecordListener createOnRecordListener() {
        return new RecordView.OnRecordListener() {
            @Override
            public void onClick() {
                takingPicture = true;
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis() + ".jpeg");
                mDataBinding.captureTips.setVisibility(View.INVISIBLE);
                imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        onFileSaved(file);
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        ToastUtils.showShort(message);
                    }
                });
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onLongClick() {
                takingPicture = false;
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis() + ".mp4");
                videoCapture.startRecording(file, new VideoCapture.OnVideoSavedListener() {
                    @Override
                    public void onVideoSaved(File file) {
                        onFileSaved(file);
                    }

                    @Override
                    public void onError(VideoCapture.UseCaseError useCaseError, String message, @Nullable Throwable cause) {
                        ToastUtils.showShort(message);
                    }
                });
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void finish() {
                videoCapture.stopRecording();
            }
        };
    }

    /**
     * 拍照/录像后的操作
     */
    private void onFileSaved(File file) {
        //扫描相册
        outputFilePath = file.getAbsolutePath();
        String mimeType = takingPicture ? "image/jpeg" : "video/mp4";
        MediaScannerConnection.scanFile(requireContext(), new String[]{outputFilePath}, new String[]{mimeType}, null);
        mViewModel.toPreview((MainActivity) mActivity, outputFilePath, !takingPicture);
    }

    /**
     * CameraX初始化操作
     */
    @SuppressLint("RestrictedApi")
    private void initCameraX() {
        CameraX.unbindAll();

        //查询一下当前要使用的设备摄像头(比如后置摄像头)是否存在
        boolean hasAvailableCameraId = false;
        //查询一下是否存在可用的cameraId.形式如：后置："0"，前置："1"
        String cameraIdForLensFacing = null;
        try {
            hasAvailableCameraId = CameraX.hasCameraWithLensFacing(mLensFacing);
            cameraIdForLensFacing = CameraX.getCameraFactory().cameraIdForLensFacing(mLensFacing);
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
        }

        if (!hasAvailableCameraId || TextUtils.isEmpty(cameraIdForLensFacing)) {
            ToastUtils.showShort("无可用的设备cameraId!,请检查设备的相机是否被占用");
            ((MainActivity) mActivity).popBackStack();
            return;
        }

        initUseCase();

        //上面配置的都是我们期望的分辨率
        List<UseCase> newUseList = new ArrayList<>();
        newUseList.add(preview);
        newUseList.add(imageCapture);
        newUseList.add(videoCapture);
        //下面我们要查询一下 当前设备它所支持的分辨率有哪些，然后再更新一下 所配置的几个usecase
        Map<UseCase, Size> resolutions = CameraX.getSurfaceManager().getSuggestedResolutions(cameraIdForLensFacing, null, newUseList);
        for (Map.Entry<UseCase, Size> next : resolutions.entrySet()) {
            UseCase useCase = next.getKey();
            Size value = next.getValue();
            Map<String, Size> update = new HashMap<>();
            update.put(cameraIdForLensFacing, value);
            useCase.updateSuggestedResolution(update);
        }
        CameraX.bindToLifecycle(this, preview, imageCapture, videoCapture);
    }

    @SuppressLint("RestrictedApi")
    private void initUseCase() {
        PreviewConfig config = new PreviewConfig.Builder()
                //设置前置/后置摄像头
                .setLensFacing(mLensFacing)
                //设置旋转角度
                .setTargetRotation(rotation)
                //设置分辨率
                .setTargetResolution(resolution)
                //宽高比
                .setTargetAspectRatio(rational)
                .build();
        preview = new Preview(config);

        imageCapture = new ImageCapture(new ImageCaptureConfig.Builder()
                .setTargetAspectRatio(rational)
                .setTargetResolution(resolution)
                .setLensFacing(mLensFacing)
                .setTargetRotation(rotation).build());

        videoCapture = new VideoCapture(new VideoCaptureConfig.Builder()
                .setTargetRotation(rotation)
                .setLensFacing(mLensFacing)
                .setTargetResolution(resolution)
                .setTargetAspectRatio(rational)
                //视频帧率
                .setVideoFrameRate(25)
                //bit率
                .setBitRate(3 * 1024 * 1024).build());

        preview.setOnPreviewOutputUpdateListener(output -> {
            //删除重新添加才能正确渲染图片/视频
            TextureView textureView = mDataBinding.textureView;
            ViewGroup viewGroup = (ViewGroup) textureView.getParent();
            viewGroup.removeView(textureView);
            viewGroup.addView(textureView, 0);

            textureView.setSurfaceTexture(output.getSurfaceTexture());
        });
    }

    /**
     * 相机合规性监测
     */
    @SuppressLint("RestrictedApi")
    private boolean checkCameraAvailable() {

        return true;
    }

    @Override
    public void onDestroy() {
        CameraX.unbindAll();
        super.onDestroy();
    }
}
