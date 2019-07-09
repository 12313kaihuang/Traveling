#### 使用方法：

1. 将本`moudle`添加进项目中

2. 在`Moudle:app`的**`build.gradle`**文件中添加如下代码：

   ```xml
   android {
     ...
     // Butterknife requires Java 8.
     compileOptions {
       sourceCompatibility JavaVersion.VERSION_1_8
       targetCompatibility JavaVersion.VERSION_1_8
     }
   }
   
   dependencies {
     ...
   
     implementation 'com.jakewharton:butterknife:10.1.0'
     annotationProcessor 'com.jakewharton:butterknife-compiler:10.1.0'
   }
   ```

3. `AndroidManifest.xml`的**`application`**中添加两句：

   ```xml
   <application
       android:appComponentFactory=""
       tools:replace="android:appComponentFactory"
       ....
   />
   ```

4. 在**`gradle.properties`**中添加两句：

   ```
   android.useAndroidX=true
   android.enableJetifier=true
   ```

   