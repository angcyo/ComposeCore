package com.angcyo.compose.basics

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/08
 */

/**使用Android原生, 弹出[toast]提示*/
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**请求忽略电池优化
 * ```
 * <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
 * ```
 * 谷歌市场提示:
 * ```
 * Use of `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` violates the Play Store Content Policy regarding acceptable use cases,
 * as described in https://developer.android.com/training/monitoring-device-state/doze-standby.html
 * ```
 * */
fun Context.requestIgnoreBatteryOptimizations() {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    intent.data = "package:${packageName}".toUri()
    startActivity(intent)
}