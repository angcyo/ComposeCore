package com.angcyo.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.angcyo.compose.core.nav.NavRouter

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2025/11/08
 */

/**启动一个带有导航的应用
 * - [Dialog]
 * - [Popup]
 * */
@Composable
fun RunNavApp(
    routerConfig: NavRouter.() -> Unit
) {
    val router = NavRouter()
    router.routerConfig()
    router.RouterBuild()
}