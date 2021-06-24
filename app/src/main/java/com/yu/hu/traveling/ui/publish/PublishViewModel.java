package com.yu.hu.traveling.ui.publish;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.ninegridlayout.entity.GridItem;
import com.yu.hu.traveling.MainActivity;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.model.Companion;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.CompanionService;
import com.yu.hu.traveling.network.NoteService;
import com.yu.hu.traveling.ui.capture.CaptureFragment;
import com.yu.hu.traveling.utils.UserManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;

import static com.yu.hu.traveling.ui.publish.PublishFragment.REQUEST_CODE_CHOOSE_PHOTO;
import static com.yu.hu.traveling.ui.publish.PublishFragment.REQUEST_CODE_CHOOSE_VIDEO;

/**
 * @author Hy
 * created on 2020/05/06 10:55
 **/
@SuppressWarnings("WeakerAccess")
public class PublishViewModel extends AndroidViewModel {
    private static final String TAG = "PublishViewModel";

    private Context mContext;

    public PublishViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void publishNote(String title, String content, int type, List<GridItem> gridItems, OnNotePublishListener listener) {
        ApiService.create(NoteService.class)
                .insert(title, content, type, UserManager.get().getUserId(), GsonUtils.toJson(gridItems))
                .enqueue(new ApiResponseCallback<Note>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Note>> call, @NotNull Note response) {
                        listener.onPublishSuccess(response);
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Note>> call, String errorMsg) {
                        listener.onPublishFailure(errorMsg);
                    }
                });
    }

    public void publishCompanion(String title, String content, String target, long targetTime, OnCompanionPublishListener listener) {
        ApiService.create(CompanionService.class)
                .insert(title, content, target, targetTime, UserManager.get().getUserId())
                .enqueue(new ApiResponseCallback<Companion>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<Companion>> call, @NotNull Companion response) {
                        listener.onPublishSuccess(response);
                    }

                    @Override
                    public void onFailure2(Call<ApiResponse<Companion>> call, String errorMsg) {
                        listener.onPublishFailure(errorMsg);
                    }
                });
    }

    public void showDatePickerDialog(FragmentManager manager, DatePickerDialog.OnDateSelectedListener listener) {
        new DatePickerDialog.Builder()
                .setOnDateSelectedListener(listener)
                .show(manager);
    }

    /**
     * 上传图片/视频
     */
    @SuppressWarnings("WeakerAccess")
    public void uploadFile(LifecycleOwner owner, List<GridItem> gridItems, @NonNull OnUploadCallback callback) {
        if (gridItems == null || gridItems.isEmpty()) {
            callback.onUploadSuccess(gridItems);
            return;
        }
        LogUtil.d(TAG, "上传 items = " + GsonUtils.toJson(gridItems));
        SparseArray<UUidModel> uuIdMap = new SparseArray<>();
        List<OneTimeWorkRequest> workRequests = new ArrayList<>();
        for (int i = 0; i < gridItems.size(); i++) {
            GridItem item = gridItems.get(i);
            UUID coverUid = null;
            UUID urlUid = null;
            if (!TextUtils.isEmpty(item.coverPath)) {
                OneTimeWorkRequest coverRequest = getOneTimeWorkRequest(item.coverPath);
                coverUid = coverRequest.getId();
                workRequests.add(coverRequest);
            }
            if (!TextUtils.isEmpty(item.urlPath)) {
                OneTimeWorkRequest urlRequest = getOneTimeWorkRequest(item.urlPath);
                urlUid = urlRequest.getId();
                workRequests.add(urlRequest);
            }
            uuIdMap.put(i, new UUidModel(coverUid, urlUid));
        }
        if (workRequests.size() == 0) {
            Log.w(TAG, "uploadFile: workRequests.size() == 0");
            callback.onUploadSuccess(gridItems);
            return;
        }
        WorkContinuation workContinuation = WorkManager.getInstance(mContext).beginWith(workRequests);
        workContinuation.enqueue();
        workContinuation.getWorkInfosLiveData().observe(owner, workInfos -> {
            //block runing enuqued failed susscess finish 这么多状态变化都会触发
            int completedCount = 0;
            int failedCount = 0;
            for (WorkInfo workInfo : workInfos) {
                WorkInfo.State state = workInfo.getState();
                Data outputData = workInfo.getOutputData();
                UUID uuid = workInfo.getId();
                if (state == WorkInfo.State.FAILED) {
                    Log.e(TAG, "uploadFile: 上传失败 - " + uuid.toString());
                    failedCount++;
                } else if (state == WorkInfo.State.SUCCEEDED) {
                    String fileUrl = outputData.getString(UploadFileWorker.KEY_URL);
                    Log.i(TAG, "uploadFile: 上传成功 - " + uuid.toString() + " - " + fileUrl);
                    updateItem(uuid, gridItems, uuIdMap, fileUrl);
                    completedCount++;
                }
            }
            if (completedCount >= workInfos.size()) {
                callback.onUploadSuccess(gridItems);
            } else if (failedCount > 0) {
                Log.e(TAG, "上传失败个数 - " + failedCount);
                callback.onUploadFailure();
            }

        });
    }

    private void updateItem(UUID uuid, List<GridItem> gridItems, SparseArray<UUidModel> uuIdMap, String fileUrl) {
        for (int i = 0; i < gridItems.size(); i++) {
            GridItem item = gridItems.get(i);
            UUidModel model = uuIdMap.get(i);
            if (model == null) {
                continue;
            }
            item.index = i;
            if (uuid.equals(model.coverUid)) {
                item.cover = fileUrl;
            } else if (uuid.equals(model.urlUid)) {
                item.url = fileUrl;
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @NotNull
    private OneTimeWorkRequest getOneTimeWorkRequest(String filePath) {
        Data inputData = new Data.Builder()
                .putString(UploadFileWorker.KEY_FILE, filePath)
                .build();

//        @SuppressLint("RestrictedApi") Constraints constraints = new Constraints();
//        //设备存储空间充足的时候 才能执行 ,>15%
//        constraints.setRequiresStorageNotLow(true);
//        //必须在执行的网络条件下才能好执行,不计流量 ,wifi
//        constraints.setRequiredNetworkType(NetworkType.UNMETERED);
//        //设备的充电量充足的才能执行 >15%
//        constraints.setRequiresBatteryNotLow(true);
//        //只有设备在充电的情况下 才能允许执行
//        constraints.setRequiresCharging(true);
//        //只有设备在空闲的情况下才能被执行 比如息屏，cpu利用率不高
//        constraints.setRequiresDeviceIdle(true);
//        //workmanager利用contentObserver监控传递进来的这个uri对应的内容是否发生变化,当且仅当它发生变化了
//        //我们的任务才会被触发执行，以下三个api是关联的
//        constraints.setContentUriTriggers(null);
//        //设置从content变化到被执行中间的延迟时间，如果在这期间。content发生了变化，延迟时间会被重新计算
        //这个content就是指 我们设置的setContentUriTriggers uri对应的内容
//        constraints.setTriggerContentUpdateDelay(0);
//        //设置从content变化到被执行中间的最大延迟时间
        //这个content就是指 我们设置的setContentUriTriggers uri对应的内容
//        constraints.setTriggerMaxContentDelay(0);
        @SuppressWarnings("UnnecessaryLocalVariable")
        OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(UploadFileWorker.class)
                .setInputData(inputData)
//                .setConstraints(constraints)
//                //设置一个拦截器，在任务执行之前 可以做一次拦截，去修改入参的数据然后返回新的数据交由worker使用
//                .setInputMerger(null)
//                //当一个任务被调度失败后，所要采取的重试策略，可以通过BackoffPolicy来执行具体的策略
//                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
//                //任务被调度执行的延迟时间
//                .setInitialDelay(10, TimeUnit.SECONDS)
//                //设置该任务尝试执行的最大次数
//                .setInitialRunAttemptCount(2)
//                //设置这个任务开始执行的时间
//                //System.currentTimeMillis()
//                .setPeriodStartTime(0, TimeUnit.SECONDS)
//                //指定该任务被调度的时间
//                .setScheduleRequestedAt(0, TimeUnit.SECONDS)
//                //当一个任务执行状态编程finish时，又没有后续的观察者来消费这个结果，难么workamnager会在
//                //内存中保留一段时间的该任务的结果。超过这个时间，这个结果就会被存储到数据库中
//                //下次想要查询该任务的结果时，会触发workmanager的数据库查询操作，可以通过uuid来查询任务的状态
//                .keepResultsForAtLeast(10, TimeUnit.SECONDS)
                .build();
        return request;
    }

    //检测输入内容
    public boolean checkInput(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort(R.string.please_input_content_hint);
            return false;
        }
        return true;
    }

    public boolean checkCompanionInput(String content, String target, long time) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort(R.string.please_input_content_hint);
            return false;
        }
        if (TextUtils.isEmpty(target)) {
            ToastUtils.showShort(R.string.target_location);
            return false;
        }
        if (time <= 0) {
            ToastUtils.showShort(R.string.target_time);
            return false;
        }
        return true;
    }

    //检测是否选择了游记
    public boolean checkType(String type, String[] noteTypes) {
        if (TextUtils.isEmpty(type)) {
            ToastUtils.showShort(R.string.choose_note_type_hint);
            return false;
        }
        for (String noteType : noteTypes) {
            if (noteType.equals(type)) return true;
        }
        ToastUtils.showShort(R.string.choose_note_type_hint);
        return false;
    }

    public void showTypeSelectDialog(FragmentManager manager, List<String> types, TypeSelectDialog.OnTypeItemSelectedListener listener) {
        new TypeSelectDialog.Builder()
                .setTypeList(types)
                .setOnTypeItemSelectedListener(listener)
                .show(manager);
    }

    public void showChoosePhotoDialog(PublishFragment fragment) {
        new ChoosePhotoDialog.Builder()
                .setOnItemClickListener(new ChoosePhotoDialog.OnItemClickListener() {
                    @Override
                    public void onTakePhotoBtnClicked(ChoosePhotoDialog dialog) {
                        ((MainActivity) fragment.requireActivity()).navigate(CaptureFragment.PAGE_URL);
                        dialog.dismiss();
                    }

                    @Override
                    public void onChoosePhotoBtnClicked(ChoosePhotoDialog dialog) {
                        choosePhoto(fragment);
                        dialog.dismiss();
                    }

                    @Override
                    public void onChooseVideoBtnClicked(ChoosePhotoDialog dialog) {
                        chooseVideo(fragment);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelBtnClicked(ChoosePhotoDialog dialog) {

                    }
                })
                .show(fragment.getChildFragmentManager());
    }

    @SuppressLint("WrongConstant")
    private void choosePhoto(PublishFragment fragment) {
        PermissionUtils.permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        //调用相册
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        fragment.startActivityForResult(intent, REQUEST_CODE_CHOOSE_PHOTO);
                    }

                    @Override
                    public void onDenied() {

                    }
                })
                .request();

    }

    @SuppressLint("WrongConstant")
    private void chooseVideo(PublishFragment fragment) {
        PermissionUtils.permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        //调用相册
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        fragment.startActivityForResult(intent, REQUEST_CODE_CHOOSE_VIDEO);
                    }

                    @Override
                    public void onDenied() {

                    }
                })
                .request();
    }

    private static class UUidModel {
        UUID coverUid;
        UUID urlUid;

        UUidModel(UUID coverUid, UUID urlUid) {
            this.coverUid = coverUid;
            this.urlUid = urlUid;
        }
    }

    public interface OnUploadCallback {
        void onUploadSuccess(List<GridItem> gridItems);

        void onUploadFailure();
    }

    public interface OnNotePublishListener {
        void onPublishSuccess(Note note);

        void onPublishFailure(String reason);
    }

    public interface OnCompanionPublishListener {
        void onPublishSuccess(Companion companion);

        void onPublishFailure(String reason);
    }
}
