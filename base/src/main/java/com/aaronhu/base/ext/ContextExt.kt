package com.aaronhu.base.ext

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * huyu create
 * 2025/3/31 17:22
 */
fun Context.readAssets2String(fileName: String): String? {
    return try {
        // 打开 assets 文件夹中的文件
        val inputStream = assets.open(fileName)
        // 使用 BufferedReader 读取文件内容
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        // 关闭输入流
        inputStream.close()
        // 返回文件内容
        stringBuilder.toString()
    } catch (e: IOException) {
        // 处理异常
        e.printStackTrace()
        null
    }
}