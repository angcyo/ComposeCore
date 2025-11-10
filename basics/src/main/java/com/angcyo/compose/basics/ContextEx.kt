package com.angcyo.compose.basics

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.core.net.toUri
import java.io.BufferedReader
import java.util.concurrent.TimeUnit

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

/**启动指定的应用程序
 *
 * 需要配置权限 ` <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"/>`
 *
 * ```
 * java.lang.NullPointerException: Parameter specified as non-null is null: method androidx.activity.ComponentActivity.startActivityForResult, parameter intent
 * ```
 * */
fun Context.startApp(packageName: String) {
    val intent =
        getAppOpenIntentByPackageName(packageName) ?: packageManager.getLaunchIntentForPackage(
            packageName
        )
    startActivity(intent)
}

/**
 * api 30, 需要在 manefist 中,声明 queries
 *
 *  获取所有应用
 *  <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"/>
 *
 * https://developer.android.com/training/basics/intents/package-visibility
 * https://developer.android.com/training/basics/intents/package-visibility#automatic
 * https://developer.android.com/about/versions/11/privacy/package-visibility
 *
 * [android.app.ApplicationPackageManager#getLaunchIntentForPackageAndCategory]
 *
 * */
@SuppressLint("WrongConstant")
fun Context.getAppOpenIntentByPackageName(packageName: String): Intent? {
    var mainActivityClass: String? = null
    val pm = packageManager
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
    val list = pm.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES)
    for (i in list.indices) {
        val info = list[i]
        if (info.activityInfo.packageName == packageName) {
            mainActivityClass = info.activityInfo.name
            break
        }
    }
    if (TextUtils.isEmpty(mainActivityClass)) {
        return null
    }
    intent.component = ComponentName(packageName, mainActivityClass!!)
    return intent
}

/**使用Root的方式启动应用程序*/
fun Context.startAppWithRoot(packageName: String) {
    val intent =
        getAppOpenIntentByPackageName(packageName) ?: packageManager.getLaunchIntentForPackage(
            packageName
        )
    if (intent != null) {
        val cmd = "am start -n $packageName/${intent.component?.className ?: ".MainActivity"}"
        Runtime.getRuntime().exec(arrayOf("su", "-c", cmd))
    }
}
