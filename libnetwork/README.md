libnetwork
==============
网络请求模块
封装`Retrofit`使其能返回`LiveData`类型的数据。

使用
--------
1. 创建bean对象
```

import androidx.annotation.Keep;
import androidx.databinding.BaseObservable;

import java.util.List;

/**
 * @author Hy
 * created on 2020/04/14 14:27
 * <p>
 * Bing每日一图返回数据
 */
@Keep
@SuppressWarnings({"unused", "WeakerAccess"})
public class BingImg extends BaseObservable {

    //bing每日一图：https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1
    public static final String REQUEST_URL = "https://cn.bing.com/";

    private static final String BASE_IMAGE_URL = "https://www.bing.com/";

    /**
     * images : [{"startdate":"20200413","fullstartdate":"202004131600","enddate":"20200414","url":"/th?id=OHR.BWFlipper_ZH-CN1813139386_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.BWFlipper_ZH-CN1813139386","copyright":"伊斯塔帕海岸的热带斑海豚，墨西哥 (© Christian Vizl/Tandem Stills + Motion)","copyrightlink":"https://www.bing.com/search?q=%E7%83%AD%E5%B8%A6%E6%96%91%E6%B5%B7%E8%B1%9A&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200413_BWFlipper%22&FORM=HPQUIZ","wp":true,"hsh":"b8381f6da75ae67b3581c0ca162718ac","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    public TooltipsBean tooltips;
    public List<ImagesBean> images;

    public static class TooltipsBean {
        /**
         * loading : 正在加载...
         * previous : 上一个图像
         * next : 下一个图像
         * walle : 此图片不能下载用作壁纸。
         * walls : 下载今日美图。仅限用作桌面壁纸。
         */

        public String loading;
        public String previous;
        public String next;
        public String walle;
        public String walls;
    }

    public static class ImagesBean extends BaseObservable {
        /**
         * startdate : 20200413
         * fullstartdate : 202004131600
         * enddate : 20200414
         * url : /th?id=OHR.BWFlipper_ZH-CN1813139386_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp
         * urlbase : /th?id=OHR.BWFlipper_ZH-CN1813139386
         * copyright : 伊斯塔帕海岸的热带斑海豚，墨西哥 (© Christian Vizl/Tandem Stills + Motion)
         * copyrightlink : https://www.bing.com/search?q=%E7%83%AD%E5%B8%A6%E6%96%91%E6%B5%B7%E8%B1%9A&form=hpcapt&mkt=zh-cn
         * title :
         * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200413_BWFlipper%22&FORM=HPQUIZ
         * wp : true
         * hsh : b8381f6da75ae67b3581c0ca162718ac
         * drk : 1
         * top : 1
         * bot : 1
         * hs : []
         */

        public String startdate;
        public String fullstartdate;
        public String enddate;
        public String url;
        public String urlbase;
        public String copyright;
        public String copyrightlink;
        public String title;
        public String quiz;
        public boolean wp;
        public String hsh;
        public int drk;
        public int top;
        public int bot;
        public List<?> hs;
    }


    /**
     * 获取每日一图的url
     *
     * @return url
     */
    public String getImgUrl() {
        if (images != null && images.size() >= 1) {
            return BASE_IMAGE_URL + images.get(0).url;
        }
        return null;
    }
}

```
2. 创建一个`Service`用于网络请求，这里以**Bing**的每日一图做例子
```
public interface SplashService {

    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    Call<BingImg> getBingImg();

    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    LiveData<BingImg> getBingImgLiveData();
}
```

3. 通过`ApiService`创建`Retrofit`对象并进行网络请求
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

Retrofit
-----------
### Url组合规则

| baseUrl                              | 和URL有关的注解中提供的值 | 最后结果                                 |
| ------------------------------------ | ------------------------- | ---------------------------------------- |
| http://localhost:4567/path/to/other/ | /post                     | http://localhost:4567/post               |
| http://localhost:4567/path/to/other/ | Post                      | http://localhost:4567/path/to/other/post |
| http://localhost:4567/path/to/other/ | https://github.com/ikidou | https://github.com/ikidou                |

规则如下：
* 如果你在注解中提供的url是完整的url，则url将作为请求的url。
* 如果你在注解中提供的url是不完整的url，且不以 / 开头，则请求的url为baseUrl+注解中提供的值
* 如果你在注解中提供的url是不完整的url，且以 / 开头，则请求的url为baseUrl的主机部分+注解中提供的值

参考文章
--------
* [LiveData+Retrofit网络请求实战](https://juejin.im/post/5d56497f518825107c565d88#heading-1)
* [Retrofit与LiveData结合](https://blog.csdn.net/JustBeauty/article/details/81120604)
* [Retrofit的Url组合规则](https://www.jianshu.com/p/d5b3ab69769b)
