package com.yu.hu.traveling.ui.splash;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.yu.hu.common.cache.CacheManager;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.libnetwork.ApiResponse;
import com.yu.hu.libnetwork.ApiService;
import com.yu.hu.traveling.model.BingImg;
import com.yu.hu.traveling.model.BottomBar;
import com.yu.hu.traveling.model.HomeTab;
import com.yu.hu.traveling.model.ImgModel;
import com.yu.hu.traveling.network.ApiResponseCallback;
import com.yu.hu.traveling.network.ConfigService;
import com.yu.hu.traveling.network.SplashService;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;

/**
 * @author Hy
 * created on 2020/04/14 13:43
 **/
@SuppressWarnings("WeakerAccess")
public class SplashViewModel extends AndroidViewModel {

    /**
     * 倒计时3s 自动跳转至主页
     */
    private static final long SPLASH_TIME_SECOND = 3L;
    private static final String TAG = SplashViewModel.class.getSimpleName();

    private Disposable subscribe;
    private MutableLiveData<Boolean> loadSuccess;  //是否加载成功
    private MutableLiveData<Long> mRestTime;  //剩余时间
    private ImgModel imgModel;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        loadSuccess = new MutableLiveData<>(false);
        mRestTime = new MutableLiveData<>(SPLASH_TIME_SECOND + 1);
        imgModel = new ImgModel();
    }

    /**
     * 开始倒计时
     */
    public void startInterval() {
        subscribe = Observable.interval(1, TimeUnit.SECONDS)
                .take(SPLASH_TIME_SECOND + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(now -> mRestTime.postValue(SPLASH_TIME_SECOND - now));
    }

    /**
     * 请求Bing每日一图并加载图片
     *
     * @param owner owner
     */
    public void loadImage(LifecycleOwner owner) {
        ImgModel model = CacheManager.get(ImgModel.CACHE_KEY, ImgModel.class);
        if (model != null && !model.needUpdate()) {
            //已经缓存的有今天的图片信息了
            Log.i(TAG, "每日一图已缓存 - " + model.getUrl());
            imgModel.setUrl(model.getUrl());
            return;
        }
        ApiService.create(BingImg.REQUEST_URL, SplashService.class)
                .getBingImgLiveData()
                .observe(owner, bingImg -> {
                    //qq授权登录回来之后出发此事件
                    if (!((Fragment) owner).isVisible()) {
                        return;
                    }
                    Log.i(TAG, "更新每日一图 - " + bingImg.getImgUrl());
                    imgModel.setUrl(bingImg.getImgUrl());
                    imgModel.save();
                });

    }

    /**
     * 从后台加载网络底部导航栏的配置信息
     */
    public void getBottomBarConfig() {
        ApiService.create(ConfigService.class).getBottomBarConfig()
                .enqueue(new ApiResponseCallback<BottomBar>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<BottomBar>> call, @NotNull BottomBar response) {
                        LogUtil.i(TAG, "缓存BottomBar配置 - " + response);
                        CacheManager.save(BottomBar.CACHE_KEY, response);
                    }
                });
//        EnqueueUtils.enqueue(call, new EnqueueUtils.SimpleCallback<BottomBar>() {
//            @Override
//            public void onResponse(BottomBar response) {
//                Log.i(TAG, "缓存BottomBar配置 - " + response);
//                CacheManager.save(BottomBar.CACHE_KEY, response);
//            }
//        }, new TypeToken<ApiResponse<BottomBar>>() {
//        }.getType());
    }

    /**
     * 加载首页tab配置信息
     */
    public void getHomeTabConfig() {
        ApiService.create(ConfigService.class).getHomeTabConfig()
                .enqueue(new ApiResponseCallback<HomeTab>() {
                    @Override
                    public void onResponse2(Call<ApiResponse<HomeTab>> call, @NotNull HomeTab response) {
                        LogUtil.i(TAG, "缓存HomeTab配置 - " + response);
                        CacheManager.save(HomeTab.CACHE_KEY, response);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
    }

    public MutableLiveData<Boolean> getLoadSuccess() {
        return loadSuccess;
    }

    public MutableLiveData<Long> getRestTime() {
        return mRestTime;
    }

    public ImgModel getImgModel() {
        return imgModel;
    }

}
