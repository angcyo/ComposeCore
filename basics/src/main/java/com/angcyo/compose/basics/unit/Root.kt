package com.angcyo.compose.basics.unit

import android.os.Build
import java.io.BufferedReader
import java.util.concurrent.TimeUnit

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/10
 */

/**判断是否有Root权限*/
fun isRoot(): Boolean {
    return try {
        Runtime.getRuntime().exec("su").waitFor() == 0
    } catch (e: Exception) {
        false
    }
}

/**
 * 5) 尝试运行 su -c id 并判断输出（能执行且 uid=0 则表明可提权）
 * 注意：运行 su 可能弹出授权提示，且可能阻塞 - 所以应在 IO 线程 / 协程中运行并使用超时
 * */
fun canExecuteSu(timeoutMillis: Long = 1000L): Boolean {
    try {
        val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
        // 等待并读取输出（带超时）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!process.waitFor(timeoutMillis, TimeUnit.MILLISECONDS)) {
                // 超时，先尝试销毁进程
                try {
                    process.destroy()
                } catch (_: Throwable) {
                }
                return false
            }
            val output = process.inputStream.bufferedReader().use(BufferedReader::readText)
            val error = process.errorStream.bufferedReader().use(BufferedReader::readText)
            // println("su out: $output err: $error")
            if (output.contains("uid=0") || output.contains("uid=0(root)")) {
                return true
            }
        }
    } catch (t: Throwable) {
        // su 不存在或被拒绝等
    }
    return false
}