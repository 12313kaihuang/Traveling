package com.aaronhu.base.ext

import android.app.Activity
import android.content.Intent

/**
 * huyu create
 * 2025/4/8 16:35
 */

fun <T> Activity.startActivity(clazz: Class<T>) {
    val intent = Intent(this, clazz)
    startActivity(intent)
}