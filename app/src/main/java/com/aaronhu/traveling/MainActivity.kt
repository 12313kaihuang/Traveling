package com.aaronhu.traveling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aaronhu.base.ext.viewModels

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //todo fun 闪屏页实现
        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition {
            //如果返回 true，则初始屏幕将保持可见,以隐藏下面的 Activity。
            !viewModel.isInitialized
        }
        viewModel.init()
    }
}
