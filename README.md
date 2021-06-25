Traveling-Jetpack
=================
毕设项目<br/>
一款基于 **Jetpack** 的旅游社交类App。是[Traveling](https://github.com/12313kaihuang/Traveling/tree/v1.0)的3.0版本，与版本相比，新版使用了
Jetpack组件库重新编写，同时新增了发布视频类型的游记功能、评论也支持加入Emoji表情。
 
Introduction
------------
* [下载APK](./app/release/Traveling1.0.apk)

### previews

<img src="./效果展示/demo.gif" width = "270" height = "585"/>


### application
* [**app**](/app) —— 程序`application`
* [test](/test)  —— 用于测试[**common**](/common)等moudle中所实现的功能点等。
### modules
  * [**common**](/common) —— 公有的`util`及`activity`等基类的封装。
  * [**emoji**](/emoji) —— 自定义表情相关控件实现，`EmojiEditText`、`EmojiTextView`等。
  * [**leancloudchatkit**](/leancloudchatkit) —— leancloud IM通讯集合包。
  * [**libnavannotation**](/libnavannotation) —— 自定义注解。
  * [**libnavcompiler**](/libnavcompiler) —— 自定义注解处理器。
  * [**libnetwork**](/libnetwork) —— 结合`Retrofit`的网络请求封装。
  * [**ninegridlayout**](/ninegridlayout) —— 自定义九宫格布局图片/视频展示控件。
  
Libraries Used
--------------
* [Architecture][10] - A collection of libraries that help you design robust, testable, and
  maintainable apps. Start with classes for managing your UI component lifecycle and handling data
  persistence.
  * [Data Binding][11] - Declaratively bind observable data to UI elements.
  * [Lifecycles][12] - Create a UI that automatically responds to lifecycle events.
  * [LiveData][13] - Build data objects that notify views when the underlying database changes.
  * [Navigation][14] - Handle everything needed for in-app navigation.
  * [Room][16] - Access your app's SQLite database with in-app objects and compile-time checks.
  * [ViewModel][17] - Store UI-related data that isn't destroyed on app rotations. Easily schedule
     asynchronous tasks for optimal execution.
  * [WorkManager][18] - Manage your Android background jobs.
* [UI][30] - Details on why and how to use UI Components in your apps - together or separate
  * [Animations & Transitions][31] - Move widgets and transition between screens.
  * [Fragment][34] - A basic unit of composable UI.
  * [Layout][35] - Lay out widgets using different algorithms.
* Custom
* Third party
  * [Glide][90] for image loading
  * [SmartRefreshLayout][50] for refresh layout
  * [AndroidUtilCode][51] for improve the development efficiency with utils

[0]: https://developer.android.com/jetpack/components
[1]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat
[2]: https://developer.android.com/kotlin/ktx
[4]: https://developer.android.com/training/testing/
[10]: https://developer.android.com/jetpack/arch/
[11]: https://developer.android.com/topic/libraries/data-binding/
[12]: https://developer.android.com/topic/libraries/architecture/lifecycle
[13]: https://developer.android.com/topic/libraries/architecture/livedata
[14]: https://developer.android.com/topic/libraries/architecture/navigation/
[16]: https://developer.android.com/topic/libraries/architecture/room
[17]: https://developer.android.com/topic/libraries/architecture/viewmodel
[18]: https://developer.android.com/topic/libraries/architecture/workmanager
[30]: https://developer.android.com/guide/topics/ui
[31]: https://developer.android.com/training/animation/
[34]: https://developer.android.com/guide/components/fragments
[35]: https://developer.android.com/guide/topics/ui/declaring-layout
[90]: https://bumptech.github.io/glide/
[91]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[50]: https://github.com/scwang90/SmartRefreshLayout
[51]: https://github.com/Blankj/AndroidUtilCode
  
License
------------
```
Copyright 2019 12313kaihuang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
