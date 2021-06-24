package com.yu.hu.traveling.ui.publish;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.yu.hu.traveling.utils.UploadManager;

/**
 * @author Hy
 * created on 2020/05/06 10:26
 * <p>
 * 上传文件到阿里云OOS
 **/
@SuppressWarnings("WeakerAccess")
public class UploadFileWorker extends Worker {

    public static final String KEY_FILE = "file";
    public static final String KEY_URL = "fileUrl";
    private static final String TAG = "UploadFileWorker";

    public UploadFileWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String filePath = getInputData().getString(KEY_FILE);

        if (TextUtils.isEmpty(filePath)) return Result.failure();
        String fileUrl = UploadManager.upload(filePath);
        if (TextUtils.isEmpty(fileUrl)) {
            Log.i(TAG, "上传文件失败 - " + filePath);
            return Result.failure();
        } else {
            Log.i(TAG, "上传文件成功 - " + filePath + ", url = " + fileUrl);
            Data outputData = new Data.Builder().putString(KEY_URL, fileUrl)
                    .build();
            return Result.success(outputData);
        }
    }

}
