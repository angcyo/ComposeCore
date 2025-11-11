package com.angcyo.compose.basics.coroutine

import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 *
 * 协程扩展相关函数
 */

fun Duration.toDelayMillis(): Long = when (isPositive()) {
    true -> plus(999_999L.nanoseconds).inWholeMilliseconds
    false -> 0L
}

/**在协程中休眠指定时长
 * - [timeMillis] 休眠的毫秒*/
suspend fun sleep(timeMillis: Long = 160, duration: Duration? = null) {
    kotlinx.coroutines.delay(duration?.toDelayMillis() ?: timeMillis)
}