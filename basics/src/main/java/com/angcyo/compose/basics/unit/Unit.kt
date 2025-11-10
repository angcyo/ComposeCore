package com.angcyo.compose.basics.unit

import java.text.SimpleDateFormat
import java.util.Date

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/10
 */

/**当前时间*/
val currentTimeMillis: Long
    get() = System.currentTimeMillis()

val nowTime: Long get() = currentTimeMillis

/**文件名中, 不能包含[:]等特殊字符 [ ]空格会应用adb pull指令
 * [yyyy-MM-dd_HH-mm-ss-SSS]
 * */
fun nowTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss.SSS"): String {
    return nowTime.toTime(pattern)
}

/**格式化时间输出*/
fun Long.toTime(pattern: String = "yyyy-MM-dd HH:mm"): String {
    val format: SimpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
    format.applyPattern(pattern)
    return format.format(Date(this))
}