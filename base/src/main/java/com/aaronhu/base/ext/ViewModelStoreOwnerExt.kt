package com.aaronhu.base.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * huyu create
 * 2025/3/27 10:51
 */
inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModels(): Lazy<T> =
    lazy { (ViewModelProvider(this)[T::class.java]) }

