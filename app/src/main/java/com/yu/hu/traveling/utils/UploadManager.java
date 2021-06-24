package com.yu.hu.traveling.utils;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blankj.utilcode.util.Utils;
import com.yu.hu.libnetwork.encrypt.IEncrypt;
import com.yu.hu.libnetwork.encrypt.RandomEncryptImpl;

/**
 * @author Hy
 * created on 2020/04/06 14:21
 * <p>
 * 阿里云OOS文件上传工具类
 **/
@SuppressWarnings("unused")
public class UploadManager {

    private static final String TAG = "UploadManager";

    //oos的bucket name
    private static final String BUCKET_NAME = "traveling-jetpack";
    private static final String ALIYUN_BUCKET_URL = "https://traveling-jetpack.oss-cn-chengdu.aliyuncs.com/";
    private static final String END_POINT = "http://oss-cn-chengdu.aliyuncs.com";//区域 可加快上传速度
    //private static final String AUTH_SERVER_URL = "http://123.56.232.18:7080/";  //鉴权服务器
    private static final String ACCESS_KEY_ID = "ETFRBS4TRHOXFDcUs2NEZCY0tHR01kYzVl";  //KEY 已做加密
    private static final String ACCESS_KEY_SECRET = "NMUFXdEVaQWQzME3h0VzkzSTFIMGdaQWZuSEVGSkpy";  //SECRET 已做加密

    private static OSSClient oss;

    static {
        IEncrypt encrypt = new RandomEncryptImpl();
        String access_key_id = encrypt.decrypt(ACCESS_KEY_ID);
        String access_key_secret = encrypt.decrypt(ACCESS_KEY_SECRET);
        OSSPlainTextAKSKCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(access_key_id, access_key_secret);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.disableLog(); //这个开启会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv

        oss = new OSSClient(Utils.getApp(), END_POINT, credentialProvider, conf);
    }

    //同步
    public static String upload(byte[] bytes) throws ClientException, ServiceException {
        String objectKey = String.valueOf(System.currentTimeMillis());
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, bytes);
        PutObjectResult result = oss.putObject(request);
        if (result.getStatusCode() == 200) {
            return ALIYUN_BUCKET_URL + objectKey;
        } else {
            return null;
        }
    }

    //异步
    public static void upload(byte[] bytes, UploadCallback callback) {
        String objectKey = String.valueOf(System.currentTimeMillis());
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, bytes);
        upload(request, callback);
    }

    //同步
    public static String upload(String filePath) {
        Log.i(TAG, "上传文件 - " + filePath);
        String objectKey = filePath.substring(filePath.lastIndexOf("/") + 1);
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, filePath);
        PutObjectResult result = null;
        try {
            result = oss.putObject(request);
        } catch (ClientException | ServiceException e) {
            e.printStackTrace();
        }
        if (result != null && result.getStatusCode() == 200) {
            return ALIYUN_BUCKET_URL + objectKey;
        } else {
            return null;
        }
    }

    //异步
    public static void upload(String filePath, UploadCallback callback) {
        String objectKey = filePath.substring(filePath.lastIndexOf("/") + 1);
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, filePath);
        upload(request, callback);
    }

    private static void upload(final PutObjectRequest put, final UploadCallback callback) {
        put.setProgressCallback((request, currentSize, totalSize) -> Log.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize));
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String eTag = result.getETag();
                String serverCallbackReturnBody = result.getServerCallbackReturnBody();
                Log.e("PutObject", "UploadSuccess" + eTag + "--" + serverCallbackReturnBody);
                if (callback != null && result.getStatusCode() == 200) {
                    callback.onUpload(ALIYUN_BUCKET_URL + put.getObjectKey());
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                printError(clientExcepion, serviceException);
                if (callback != null) {
                    callback.onError(serviceException.getRawMessage());
                }
            }
        });
    }

    public void download(String url, final String filePath, final DownloadCallback callback) {
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(BUCKET_NAME, url);
        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                Log.d("Content-Length", "" + result.getContentLength());
                //FileUtil.SaveFile(filePath, result.getObjectContent());
                if (callback != null) {
                    callback.onDownloadSuccess(filePath);
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                printError(clientExcepion, serviceException);
                if (callback != null) {
                    callback.onError(serviceException.getRawMessage());
                }
            }
        });

    }

    private static void printError(ClientException clientExcepion, ServiceException serviceException) {
        // 请求异常
        if (clientExcepion != null) {
            // 本地异常如网络异常等
            clientExcepion.printStackTrace();
        }
        if (serviceException != null) {
            // 服务异常
            Log.e("ErrorCode", serviceException.getErrorCode());
            Log.e("RequestId", serviceException.getRequestId());
            Log.e("HostId", serviceException.getHostId());
            Log.e("RawMessage", serviceException.getRawMessage());
        }
    }

    public interface UploadCallback {
        void onUpload(String result);

        void onError(String error);
    }

    public interface DownloadCallback {
        void onDownloadSuccess(String fileUrl);

        void onError(String error);
    }
}
