package com.angcyo.compose.basics.unit

import android.Manifest
import android.content.Context
import android.net.wifi.WifiManager
import android.widget.TextView
import androidx.collection.SimpleArrayMap
import com.angcyo.compose.basics.global.app
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

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

/**创建一个类的实例*/
fun <T> Class<T>.createInstance(): T {
    return try {
        newInstance()
    } catch (e: Exception) {
        getDeclaredConstructor().newInstance()
    }
}

/**堆栈信息保存到文件*/
fun Throwable.saveTo(filePath: String, append: Boolean = true) {
    val pw = PrintWriter(BufferedWriter(FileWriter(filePath, append)))
    printStackTrace(pw)
    pw.close()
}

/**枚举[SimpleArrayMap]*/
fun <K, V> SimpleArrayMap<K, V>.each(action: (key: K, value: V?) -> Unit) {
    val size = size()
    for (i in 0 until size) {
        val k = keyAt(i)
        val v = get(k)
        action(k, v)
    }
}

/**
 * 获取wifi ip地址
 *
 * [android.Manifest.permission.ACCESS_WIFI_STATE]
 * [android.text.format.Formatter.formatIpAddress]
 */
fun getWifiIP(): String? {
    return try {
        val context = app().applicationContext
        if (context.havePermissions(Manifest.permission.ACCESS_WIFI_STATE)) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            //ipAddress 为0时, 有可能wifi被禁用, 或者未连接. 也有可能是正在连接
            //<unknown ssid>
            //android.text.format.Formatter.formatIpAddress(ipAddress)
            String.format(
                Locale.getDefault(), "%d.%d.%d.%d",
                ipAddress and 0xff, ipAddress shr 8 and 0xff,
                ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff
            )
        } else {
            null
        }
    } catch (ex: Exception) {
        //Log.e(TAG, ex.getMessage());
        null
    }
}

/**将[value]限制在[min] [max]之间*/
fun clamp(value: Float, min: Float, max: Float): Float = min(max(value, min), max)

fun clamp(value: Int, min: Int, max: Int): Int = min(max(value, min), max)

fun clamp(value: Long, min: Long, max: Long): Long = min(max(value, min), max)

fun Any?.string(def: CharSequence = ""): CharSequence {
    return when {
        this == null -> return def
        this is TextView -> text ?: def
        this is CharSequence -> this
        else -> this.toString()
    }
}

fun Any?.str(def: String = ""): String {
    return if (this == null) {
        return def
    } else if (this is String) {
        this
    } else {
        this.toString()
    }
}