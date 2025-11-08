package com.angcyo.compose.basics

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/08
 *
 * Android 显示通知需要在`manifest`中配置对应的权限
 *
 * ```
 * <!--notification 发送通知需要的权限-->
 * <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
 * ```
 */
object NotificationHelper {

    /**检查通知是否被关闭*/
    fun isNotificationEnabled(context: Context): Boolean {
        val nmCompat = NotificationManagerCompat.from(context)
        val appEnabled = nmCompat.areNotificationsEnabled()
        return appEnabled
        /*return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val nm = context.getSystemService(NotificationManager::class.java)
            nm.areNotificationsEnabled()
        } else {
            true
        }*/
    }

    /**检查指定通道的通知权限*/
    fun isChannelEnabled(context: Context, channelId: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val nm = context.getSystemService(NotificationManager::class.java)
            val channel = nm.getNotificationChannel(channelId)
            return channel?.importance != NotificationManager.IMPORTANCE_NONE
        }
        return true
    }

    /**请求通知权限*/
    fun requestNotificationPermission(activity: Activity?, requestCode: Int = 9292) {
        if (activity == null) {
            return
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    requestCode
                )
            } else {
                //openNotificationChannelSettings(activity, channelId)
            }
        } else {
            // 已授权，可发送通知
        }
    }

    /**打开通知通道设置*/
    fun openNotificationChannelSettings(activity: Activity?, channelId: String) {
        if (activity == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
            }
            activity.startActivity(intent)
        } else {
            // 低版本跳转到应用详情
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
            }
            activity.startActivity(intent)
        }
    }

    /**创建一个通知通道*/
    fun createNotificationChannel(
        context: Context,
        channelId: String = "",
        channelName: CharSequence = "",
        channelImportance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        channelDescription: String = ""
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                channelId,
                channelName,
                channelImportance
            )
            channel.description = channelDescription
            nm.createNotificationChannel(channel)
        }
    }

    /**显示一个通知*/
    fun showNotification(
        context: Context,
        channelId: String,
        id: Int,
        title: CharSequence = "",
        text: CharSequence = "",
        intent: PendingIntent? = null,
        icon: Int = android.R.drawable.ic_lock_idle_alarm,
        action: NotificationCompat.Builder.() -> Unit = {}
    ): Notification {
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(icon)
            .setContentIntent(intent)
            .apply(action)
            .build()
        val nm = context.getSystemService(NotificationManager::class.java)
        nm.notify(id, notification)
        return notification
    }

}