package com.aaronhu.base.utils

/**
 * huyu create
 * 2025/3/17 11:58
 */
interface ALogger {
    fun d(tag: String, msg: String)
    fun i(tag: String, msg: String)
    fun w(tag: String, msg: String, throwable: Throwable? = null)
    fun e(tag: String, msg: String, throwable: Throwable? = null)
}