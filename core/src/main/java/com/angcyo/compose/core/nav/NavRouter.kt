package com.angcyo.compose.core.nav

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.angcyo.compose.basics.annotation.Api
import com.angcyo.compose.basics.annotation.Property
import kotlinx.serialization.Serializable

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/10
 *
 * Navigation 3
 *
 * https://developer.android.google.cn/guide/navigation/navigation-3?hl=zh-cn
 *
 * - [NavDisplay]
 */
class NavRouter {

    /**路由表*/
    @Property
    val routeList = mutableListOf<SceneRoute>()

    /**路由内容映射, 路由[SceneRoute.path]对应的*/
    @Property
    val routeMap = mutableMapOf<String, @Composable () -> Unit>()

    /**定义一个路由*/
    @Api
    fun route(path: String, name: String = "", content: @Composable () -> Unit) {
        routeList.add(SceneRoute(path, name))
        routeMap[path] = content
    }

    @Api
    operator fun set(path: String, name: String = "", content: @Composable () -> Unit) {
        route(path, name, content)
    }

    /**
     * - [SceneStrategy]
     *  - [SinglePaneSceneStrategy]
     *  - [DialogSceneStrategy]
     *  - [TwoPaneSceneStrategy]
     * */
    @Api
    @Composable
    fun RouterBuild() {
        if (routeList.isEmpty()) {
            routeList.add(SceneRoute("/"))
        }
        val backStack = rememberNavBackStack(*routeList.toTypedArray())
        CompositionLocalProvider(
            LocalNavBackStack provides backStack,
            LocalNavRouter provides this
        ) {
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
                entryProvider = entryProvider({ unknownScreen ->
                    NavEntry(unknownScreen) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFFF0000))/*背景颜色ARGB*/
                                .border(1.dp, Color.Magenta) /*边框*/,
                            contentAlignment = Alignment.Center
                        ) {
                            val text = if (unknownScreen is SceneRoute) {
                                "Unknown ${unknownScreen.path}"
                            } else {
                                "Unknown $unknownScreen"
                            }
                            Text(
                                text,
                                modifier = Modifier.clickable(onClick = { backStack.removeLastOrNull() })
                            )
                        }
                    }
                }) {
                    entry<SceneRoute> { route ->
                        val content = routeMap[route.path]
                        if (content == null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "404 ${route.path}",
                                    modifier = Modifier.clickable(onClick = { backStack.removeLastOrNull() })
                                )
                            }
                        } else {
                            content()
                        }
                    }
                },
            )
        }
    }
}

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
) : NavKey

/**App导航回退栈
 *
 * - [NavBackStack.add] 添加一个导航
 * - [NavBackStack.remove].[NavBackStack.removeLastOrNull] 移除最后一个导航
 * */
val LocalNavBackStack = compositionLocalOf<NavBackStack<NavKey>?> { null }

/**[NavRouter]*/
val LocalNavRouter = compositionLocalOf<NavRouter?> { null }



