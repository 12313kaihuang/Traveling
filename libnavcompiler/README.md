libnavcompiler
======================
自定义`Destination`注解处理器，动态生成json配置文件，
利用json文件可动态创建Navigation导航。
生成的json文件在`app/src/main/assets/`目录下

使用
---------
1. 项目中引用
```
dependencies {
    ...

    //自定义注解处理器
    implementation project(path: ':libnavannotation')
    annotationProcessor project(":libnavcompiler")
}

```

2. 使用注解
```java
@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
public class HomeFragment extends DevelopingFragment {

    @Override
    protected String getText() {
        return getString(R.string.home);
    }
}

@FragmentDestination(pageUrl = "main/tabs/information", needLogin = true)
public class InformationFragment extends DevelopingFragment {

    @Override
    protected String getText() {
        return getString(R.string.information);
    }
}
```

3. `build project`，生成`json`文件
```json
{
    "main/tabs/home":{
        "isFragment":true,
        "asStarter":true,
        "needLogin":false,
        "pageUrl":"main/tabs/home",
        "className":"com.yu.hu.traveling.ui.home.HomeFragment",
        "id":1846910234
    },
    "main/tabs/information":{
        "isFragment":true,
        "asStarter":false,
        "needLogin":true,
        "pageUrl":"main/tabs/information",
        "className":"com.yu.hu.traveling.ui.infomation.ChatListFragment",
        "id":1639132772
    }
}
```