package com.angcyo.compose.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.angcyo.compose.basics.LibApplication
import com.angcyo.compose.basics.annotation.Initial
import com.angcyo.compose.core.objectbox.RObjectBox

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 */
open class CoreApplication : LibApplication(), ViewModelStoreOwner {

    override fun onCreate() {
        super.onCreate()

        //初始化
        initCoreApplication()
    }

    @Initial
    override fun initLibApplication() {
        super.initLibApplication()
    }

    @Initial
    open fun initCoreApplication() {
        RObjectBox.initObjectBox(this)
    }

    //<editor-fold desc="ViewModelStore">

    /**[ViewModel]单例*/
    val modelStore: ViewModelStore = ViewModelStore()

    override val viewModelStore get() = modelStore

    //</editor-fold desc="ViewModelStore">
}