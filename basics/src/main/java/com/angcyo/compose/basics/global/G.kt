package com.angcyo.compose.basics.global

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.view.Window
import androidx.lifecycle.LifecycleOwner
import com.angcyo.compose.basics.BuildConfig
import com.angcyo.compose.basics.annotation.Config
import com.angcyo.compose.basics.annotation.GlobalInstance
import com.angcyo.compose.basics.annotation.Initial
import com.angcyo.compose.basics.global.RBackground.lastActivityRef
import com.angcyo.compose.basics.hawk.RHawk
import com.angcyo.compose.basics.unit.L
import com.angcyo.compose.basics.unit.getAppString

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 *
 * 一些全局变量
 */
object G {
    @GlobalInstance
    var application: Context? = null

    @Config
    var isGDebug = false

    /**全局初始化*/
    @Initial
    fun initGlobal(context: Context? = null) {
        application = context?.applicationContext ?: application
        RHawk.initHawk(context)

        L.init(getAppString("app_name") ?: "Log", isDebug())
    }

}


/**[Activity]*/
val lastActivity: Activity?
    get() = lastActivityRef?.get()

/**[Context]*/
val lastContext: Context
    get() = lastActivity ?: app()

/**包名*/
val lastPackageName: String
    get() = lastContext.packageName

val lastWindow: Window?
    get() = lastActivity?.window

val lastDecorView: View?
    get() = lastWindow?.decorView

/**[LifecycleOwner]*/
val lastLifecycleOwner: LifecycleOwner?
    get() = if (lastContext is LifecycleOwner) {
        lastContext as LifecycleOwner
    } else if (app() is LifecycleOwner) {
        app() as LifecycleOwner
    } else {
        null
    }

/**反射获取[Application]对象
 * [android.app.ActivityThread#currentApplication]*/
fun currentApplication(): Application? {
    return try {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val method = activityThreadClass.getMethod("currentApplication")
        method.invoke(null) as Application?
    } catch (e: Exception) {
        null
    }
}

fun app(): Context = G.application ?: (LibInitProvider.contentProvider)?.apply {
    RHawk.initHawk(this)
} ?: currentApplication()?.apply {
    RHawk.initHawk(this)
} ?: PlaceholderApplication().apply {
    Log.e("PlaceholderApplication", "application 未初始化")
}

/**库打包成aar之后, [BuildConfig.DEBUG] 是 release*/
fun isDebug() = BuildConfig.DEBUG || G.isGDebug /*|| isAppDebug() || isDebugType()*/