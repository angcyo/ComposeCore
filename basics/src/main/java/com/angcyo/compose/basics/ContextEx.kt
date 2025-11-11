package com.angcyo.compose.basics

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.app.KeyguardManager.KeyguardDismissCallback
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.core.net.toUri
import com.angcyo.compose.basics.annotation.Api
import com.angcyo.compose.basics.global.lastActivity
import com.angcyo.compose.basics.global.lastContext
import com.angcyo.compose.basics.unit.L
import com.angcyo.compose.basics.unit.invokeMethod

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/08
 */

/**使用Android原生, 弹出[toast]提示*/
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun toastQQ(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    lastContext.toast(message, duration)
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
fun Context.startApp(packageName: String): Boolean {
    try {
        val intent =
            getAppOpenIntentByPackageName(packageName) ?: packageManager.getLaunchIntentForPackage(
                packageName
            )
        if (intent == null) {
            L.w("未找到启动的应用程序[$packageName]")
            return false
        }
        startActivity(intent)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
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

/**使用Root的方式启动应用程序
 *
 * ```
 * Caused by: java.io.IOException: Cannot run program "su": error=2, No such file or directory
 * ```
 * */
fun Context.startAppWithRoot(packageName: String): Boolean {
    val intent =
        getAppOpenIntentByPackageName(packageName) ?: packageManager.getLaunchIntentForPackage(
            packageName
        )
    if (intent != null) {
        val cmd = "am start -n $packageName/${intent.component?.className ?: ".MainActivity"}"
        return execAsRoot(cmd)
    } else {
        L.w("未找到启动的应用程序[$packageName]")
    }
    return false
}

/**使用Root的方式执行命令*/
@Api
fun execAsRoot(cmd: String): Boolean {
    try {
        val p = Runtime.getRuntime().exec(arrayOf("su", "-c", cmd))
        p.waitFor()
        return p.exitValue() == 0
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**亮屏或灭屏操作
 *
 * - [timeoutMs] 亮屏时有效
 * - [accService] 灭屏时有效
 * */
@Api
fun turnScreenOnOff(
    on: Boolean = true, timeoutMs: Long = 3000L, accService: AccessibilityService? = null,
) {
    val activity = lastActivity
    if (on) {
        //亮屏 //（唤醒）
        if (!execAsRoot("input keyevent 224")) {
            if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                activity.setTurnScreenOn(true)
            } else {
                val pm = lastContext.getSystemService(Context.POWER_SERVICE) as PowerManager
                if (!pm.isInteractive) {
                    // 下面的标志组合会唤醒屏幕并使其变亮；注意某些标志已被标注为废弃但仍可工作
                    val wakeLock = pm.newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                        "MyApp:WakeLockTag"
                    )
                    wakeLock.acquire(timeoutMs)
                    // 自动释放或手动 release()
                    // wakeLock.release() // 如果需要立刻释放
                }
            }
        }
    } else {
        //灭屏 //（使设备睡眠）
        if (!execAsRoot("input keyevent 223")) {
            // inside an AccessibilityService subclass
            if (accService != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // 需测试并确认可用性
                accService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)
            } else if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                activity.setTurnScreenOn(false)
            }
        }
    }
}

/**
 * 唤醒手机屏幕并解锁, 点亮屏幕,解锁手机
 */
@SuppressLint("MissingPermission")
@JvmOverloads
fun wakeUpAndUnlock(
    context: Context,
    wakeLock: Boolean = true /*亮屏(并解锁) or 灭屏*/,
    succeededAction: Runnable? = null
) {
    // 获取电源管理器对象
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val screenOn: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
        pm.isInteractive
    } else {
        pm.isScreenOn
    }
    if (wakeLock) {
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            val wl = pm.newWakeLock( /*PowerManager.FULL_WAKE_LOCK |*/
                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK /*PowerManager.SCREEN_BRIGHT_WAKE_LOCK*/,
                context.packageName + ":bright"
            )
            wl.acquire(10000) // 点亮屏幕
            wl.release() // 释放
        } else {
            //屏幕已经是亮的
            if (succeededAction != null) {
                val handler = Handler(Looper.getMainLooper())
                handler.post(succeededAction)
            }
        }
        // 屏幕解锁
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (keyguardManager.isKeyguardLocked) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && context is Activity) {
                //在未设置密码的情况下, 可以解锁
                keyguardManager.requestDismissKeyguard(
                    context, object : KeyguardDismissCallback() {
                        override fun onDismissError() {
                            super.onDismissError()
                            //如果设备未锁屏的情况下, 调用此方法.会回调错误
                            L.i("onDismissError")
                        }

                        override fun onDismissSucceeded() {
                            super.onDismissSucceeded()
                            //输入密码解锁成功
                            L.i("onDismissSucceeded")
                            if (succeededAction != null) {
                                val handler = Handler(Looper.getMainLooper())
                                handler.post(succeededAction)
                            }
                        }

                        override fun onDismissCancelled() {
                            super.onDismissCancelled()
                            //弹出密码输入框之后, 取消了会回调
                            L.i("onDismissCancelled")
                        }
                    })
            } else {
                val keyguardLock = keyguardManager.newKeyguardLock("unLock")
                // 屏幕锁定
                keyguardLock.reenableKeyguard()
                keyguardLock.disableKeyguard() // 解锁
                L.i("reenableKeyguard -> disableKeyguard")
                if (succeededAction != null) {
                    val handler = Handler(Looper.getMainLooper())
                    handler.post(succeededAction)
                }
            }
        }
    } else {
        if (screenOn) {
//                PowerManager.WakeLock wl = pm.newWakeLock(
//                        PowerManager.PARTIAL_WAKE_LOCK,
//                        context.getPackageName() + ":bright");
//                wl.acquire();
//                wl.release();
            //android.permission.DEVICE_POWER
            pm.invokeMethod("goToSleep", SystemClock.uptimeMillis(), 0, 0)
        }
    }
}
