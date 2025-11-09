package com.angcyo.compose.core

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2025/11/08
 */

/**定义一个场景路由
 * - 页面
 *
 * ```
 * kotlinx.serialization.SerializationException: Serializer for class 'SceneRoute' is not found.
 * Please ensure that class is marked as '@Serializable' and that the serialization compiler plugin is applied.
 * ```
 * */
@Serializable
data class SceneRoute(
    /**路由路径*/
    val path: String = "",
    /**路由名称*/
    val name: String = "",
    /**路由的界面内容*/
    @kotlinx.serialization.Transient val content: @Composable () -> Unit = {},
) : NavKey

/**404页面*/
var unknownScene = SceneRoute(
    path = "/404",
    name = "Unknown",
    content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFF0000))/*背景颜色ARGB*/
                .border(1.dp, Color.Magenta) /*边框*/, contentAlignment = Alignment.Center
        ) {
            Text("Unknown route")
        }
    },
)

/**App导航回退栈
 *
 * - [NavBackStack.add] 添加一个导航
 * - [NavBackStack.remove].[NavBackStack.removeLastOrNull] 移除最后一个导航
 * */
val LocalNavBackStack = compositionLocalOf<NavBackStack<NavKey>?> { null }

/**启动一个带有导航的应用
 * - [Dialog]
 * - [Popup]
 * */
@Composable
@PreviewScreenSizes
@Keep
fun RunNavApp(
    routers: List<NavKey> = listOf(
        SceneRoute(
            "/",
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(), // 背景颜色
                    contentAlignment = Alignment.Center
                ) {
                    val backStack = LocalNavBackStack.current;
                    Text(
                        text = "Hello Jetpack Compose",
                        modifier = Modifier.clickable(onClick = { backStack?.removeLastOrNull() })
                    )
                }
            },
        )
    ),
) {
    val backStack = rememberNavBackStack(*routers.toTypedArray())
    CompositionLocalProvider(LocalNavBackStack provides backStack) {
        NavDisplay(
            backStack,
            entryDecorators = listOf(
                // Add the default decorators for managing scenes and saving state
                rememberSaveableStateHolderNavEntryDecorator(),
                // Then add the view model store decorator
                rememberViewModelStoreNavEntryDecorator()
            ),
            onBack = {
                if (backStack.isNotEmpty()) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider({
                val scene = unknownScene
                NavEntry(scene) {
                    scene.content()
                }
            }) {
                entry<SceneRoute> {
                    it.content()
                }
            },
        )
    }
}