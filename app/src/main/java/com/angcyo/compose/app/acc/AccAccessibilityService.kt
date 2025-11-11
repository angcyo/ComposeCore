package com.angcyo.compose.app.acc

import com.angcyo.compose.basics.unit.openApp

/**
 * 无障碍服务
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/07/26
 * Copyright (c) 2020 angcyo. All rights reserved.
 */
open class AccAccessibilityService : BaseAccService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        openApp()
    }
}