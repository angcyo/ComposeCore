package com.angcyo.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.angcyo.compose.core.nav.NavRouter
import kotlinx.coroutines.CoroutineScope

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2025/11/08
 */

/**启动一个带有导航的应用
 *
 * ```kotlin
 * RunNavApp {
 *     this["/"] = { KeepAliveScreen() }
 *     route("/") {
 *         KeepAliveScreen()
 *     }
 * }
 * ```
 *
 * - [Dialog]
 * - [Popup]
 * */
@Composable
fun RunNavApp(
    routerConfig: @Composable NavRouter.() -> Unit
) {
    //协程
    val coroutineScope = rememberCoroutineScope()

    //导航
    val router = NavRouter()
    router.routerConfig()

    CompositionLocalProvider(
        LocalAppCoroutineScope provides coroutineScope,
    ) {
        router.RouterBuild()
    }
}

/**
 * App级别的协程作用域
 * [CoroutineScope]*/
val LocalAppCoroutineScope = compositionLocalOf<CoroutineScope?> { null }
