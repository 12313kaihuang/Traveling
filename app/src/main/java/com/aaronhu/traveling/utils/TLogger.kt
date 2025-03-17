package com.aaronhu.traveling.utils

import android.util.Log
import com.aaronhu.base.utils.ALogger

/**
 * huyu create
 * 2025/3/17 16:05
 */
object TLogger : ALogger {

    private const val TAG = "Traveling"

    override fun d(tag: String, msg: String) {
        Log.d("$TAG#$tag", msg)
    }

    override fun i(tag: String, msg: String) {
        Log.i("$TAG#$tag", msg)
    }

    override fun w(tag: String, msg: String, throwable: Throwable?) {
        throwable?.let {
            Log.w("$TAG#$tag", msg, it)
        } ?: {
            Log.w("$TAG#$tag", msg)
        }
    }

    override fun e(tag: String, msg: String, throwable: Throwable?) {
        throwable?.let {
            Log.e("$TAG#$tag", msg, it)
        } ?: {
            Log.e("$TAG#$tag", msg)
        }
    }

}