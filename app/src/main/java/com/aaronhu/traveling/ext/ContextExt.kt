package com.aaronhu.traveling.ext

import android.content.Context
import com.aaronhu.base.ext.readAssets2String
import com.aaronhu.base.utils.GsonUtils

/**
 * huyu create
 * 2025/3/31 17:22
 */
//todo opt 业务逻辑 感觉可以单独存放
inline fun <reified T> Context.readAssets(fileName: String): T? =
    readAssets2String(fileName)?.let { GsonUtils.fromJson(it) }