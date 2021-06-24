libnetwork
==============
网络请求模块
封装`Retrofit`使其能返回`LiveData`类型的数据。

使用
--------
1. 创建一个`Service`用于网络请求，这里以**Bing**的每日一图做例子
```
public interface SplashService {

    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    Call<BingImg> getBingImg();

    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    LiveData<BingImg> getBingImgLiveData();
}
```

2. 通过`ApiService`创建`Retrofit`对象并进行网络请求
```
ApiService.create("https://cn.bing.com/", SplashService.class)
        .getBingImgLiveData().observe(this, new Observer<BingImg>() {
    @Override
    public void onChanged(BingImg bingImg) {
        Log.d(TAG, "onChanged: bingImg = " + bingImg);
    }
});
```

当然你仍然可以使用原来的`Call`作为返回类型，使用示例：
```
ApiService.create("https://cn.bing.com/", SplashService.class)
        .getBingImg().enqueue(new Callback<BingImg>() {
    @Override
    public void onResponse(@NonNull Call<BingImg> call, @NonNull Response<BingImg> response) {

        Log.i("aaa", "onResponse: " + response.body().getImgUrl());

    }

    @Override
    public void onFailure(@NonNull Call<BingImg> call, @NonNull Throwable t) {
        Log.i("aaa", "onFailure: 请求失败");
    }
});
```

参考文章
--------
* [LiveData+Retrofit网络请求实战](https://juejin.im/post/5d56497f518825107c565d88#heading-1)
* [Retrofit与LiveData结合](https://blog.csdn.net/JustBeauty/article/details/81120604)
