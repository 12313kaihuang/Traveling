### Traveling

#### 大创项目——基于推荐系统的游记攻略一站式旅游社交平台  

在学习了一些`RxJava`和`MVP`架构的一些知识后决定利用所学来重构一下这个项目，详细项目简介可跳转至老版本的[README](https://github.com/12313kaihuang/Traveling/tree/old_version)。    

重构暂未完成，[原版本](https://github.com/12313kaihuang/Traveling/tree/old_version)有详细介绍与部分截图，传送门：[old_version](https://github.com/12313kaihuang/Traveling/tree/old_version)   

#### 重构内容：

* 封装`Application`，`Activity`以及`Fragment`基类并结合上`MVP`架构，简化实际使用过程中的通用操作，封装代码均统一放在[library]()这个`moudle`中。

* 使用到了`RxJava`异步请求数据与初始化操作，优化使用体验。

* 使用到了`ButterKnife`和`EventBus`替代传统的`findViewById`以及动态广播注册的操作。

* 自定义了`Dialog`的[Builder]()对象用于简化自定义`Dialog`的流程，示例代码：

  ```java
  new HLoadingDialog.Builder(this)
           .setHint("正在登陆..")
           .setIndeterminateDrawable(R.drawable.progress_bar)
           .setLifeCycleListener(new LifeCycleListener() {
                  @Override
                  public void onCanceled(Dialog dialog) {
                              //...
                  }
           })
           .build()
           .show();
  ```

* 使用了`AndroidUtilCode`框架简化一些如启动`Activity`，`Toast`等功能的实现。

* 自定义了一些[工具包]()，用于封装`Retrofit`的`Service`的获取（并使用`HashMap`进行缓存）、`Gson`单例对象的获取与转换等、`Observer`的转换等。

* 使用容联云通讯替代`LeanCloud`的`IM SDK`。

* 使用`GreenDao`替代原有的`Litepal`框架，并且不再仅存当前登录的用户信息，而是缓存聊天列表中用户头像，昵称等信息并且会设有过期时间，超时后会刷新缓存。