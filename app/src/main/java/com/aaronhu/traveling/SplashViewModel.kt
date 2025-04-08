package com.aaronhu.traveling

import androidx.lifecycle.viewModelScope
import com.aaronhu.traveling.base.TravelingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * huyu create
 * 2025/3/27 10:38
 */
class SplashViewModel : TravelingViewModel() {

    fun init(block: () -> Unit) {
        viewModelScope.launch {
            delay(1500)
            block()
        }
    }

//    var isInitialized = false
//        private set
//
//    fun init() {
//        viewModelScope.launch {
//            delay(1500)
//            isInitialized = true
//        }
//    }

}