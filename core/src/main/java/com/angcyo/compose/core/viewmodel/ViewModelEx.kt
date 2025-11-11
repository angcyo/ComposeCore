package com.angcyo.compose.core.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.angcyo.compose.basics.global.app
import com.angcyo.compose.core.CoreApplication


/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/12/24
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */

fun coreApp() = app() as CoreApplication

//<editor-fold desc="Application级别的单例模式">

fun Activity.core(action: CoreApplication.() -> Unit = {}): Application {
    if (application is CoreApplication) {
        (application as CoreApplication).action()
    }
    return application
}

/**[CoreApplication]中的[ViewModel]*/
inline fun <reified VM : ViewModel> Activity.vmCore(): VM {
    return ViewModelProvider(
        coreApp(),
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )[VM::class.java]
}

/**返回[CoreApplication]级别的[ViewModel]*/
inline fun <reified VM : ViewModel> vmCore(): VM {
    return ViewModelProvider(
        coreApp(),
        ViewModelProvider.AndroidViewModelFactory.getInstance(app() as Application)
    )[VM::class.java]
}

inline fun <reified VM : ViewModel> vmApp(): VM = vmCore()

//</editor-fold desc="Application级别的单例模式">