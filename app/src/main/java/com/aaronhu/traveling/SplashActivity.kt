package com.aaronhu.traveling

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aaronhu.base.ext.startActivity
import com.aaronhu.base.ext.viewModels
import com.aaronhu.traveling.ui.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //todo huyu fun SplashScreen接入？
//        splashScreen.setKeepOnScreenCondition {
//            //如果返回 true，则初始屏幕将保持可见,以隐藏下面的 Activity。
//            !viewModel.isInitialized
//        }
        viewModel.init {
            startActivity(MainActivity::class.java)
        }
    }
}
