package com.aaronhu.base.utils

import com.google.gson.Gson

/**
 * huyu create
 * 2025/3/31 16:59
 */
object GsonUtils {

    private val gson = Gson()

    fun <T> toJson(obj: T): String {
        return gson.toJson(obj)
    }

    inline fun <reified T> fromJson(json: String) = fromJson(json, T::class.java)

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }
}
