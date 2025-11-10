package com.angcyo.compose.core.nav

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import com.angcyo.compose.basics.annotation.Api
import com.angcyo.compose.basics.annotation.Config
import com.angcyo.compose.basics.annotation.Property
import com.angcyo.compose.basics.unit.L
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

    companion object {
        /**默认的主页*/
        const val INITIAL_PATH: String = "/"
    }

    /**路由表*/
    @Property
    val routeList = mutableListOf<ScreenRoute>()

    /**路由内容映射, 路由[ScreenRoute.path]对应的*/
    @Property
    val routeMap = mutableMapOf<String, @Composable () -> Unit>()

    @Config
    lateinit var initialPath: String;

    /**添加一个路由*/
    @Api
    fun route(
        path: String,
        name: String? = null,
        label: String? = null,
        content: @Composable () -> Unit
    ) {
        routeList.add(ScreenRoute(path, name, label))
        routeMap[path] = content
    }

    @Api
    operator fun set(
        path: String,
        name: String? = null,
        label: String? = null,
        content: @Composable () -> Unit
    ) {
        route(path, name, label, content)
    }

    //--

    /**推进一个路由*/
    @Api
    fun push(
        navBack: NavBackStack<NavKey>?,
        route: ScreenRoute?,
        path: String? = null,
        name: String? = null
    ) {
        val route = route ?: routeList.find {
            if (path != null) {
                it.path == path
            } else if (name != null) {
                it.name == name
            } else {
                false
            }
        }
        //--
        if (route != null) {
            navBack?.add(route)
        } else {
            navBack?.add(ScreenRoute(path ?: name ?: "/404", name))
        }
    }

    //--

    /**
     * - [initialPath] 路由初始化路径
     *
     * - [SceneStrategy]
     *  - [SinglePaneSceneStrategy]
     *  - [DialogSceneStrategy]
     *  - [TwoPaneSceneStrategy]
     * */
    @Api
    @Composable
    fun RouterBuild(initialPath: String = INITIAL_PATH) {
        this.initialPath = initialPath
        if (routeList.isEmpty()) {
            routeList.add(ScreenRoute(INITIAL_PATH))
        }
        //导航
        val backStack = rememberNavBackStack(
            /**routeList.toTypedArray()*/
            routeList.find { it.path == initialPath } ?: routeList.first()
        )
        CompositionLocalProvider(
            LocalNavBackStack provides backStack,
            LocalNavRouter provides this,
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
                    L.d("路由Back")
                    if (backStack.isNotEmpty()) {
                        backStack.removeLastOrNull()
                    }
                },
                transitionSpec = {
                    ContentTransform(
                        fadeIn(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                        fadeOut(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                    )
                },
                popTransitionSpec = {
                    ContentTransform(
                        fadeIn(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                        fadeOut(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                    )
                },
                predictivePopTransitionSpec = {
                    ContentTransform(
                        fadeIn(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                        fadeOut(animationSpec = tween(DEFAULT_TRANSITION_DURATION_MILLISECOND)),
                    )
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
                            val text = if (unknownScreen is ScreenRoute) {
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
                    entry<ScreenRoute> { route ->
                        // 路由内容
                        L.d("显示路由->${route}")
                        CompositionLocalProvider(LocalCurrentRoute provides route) {
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
data class ScreenRoute(
    /**路由的路径*/
    val path: String = "",
    /**路由的名称*/
    val name: String? = null,
    /**路由显示的标签*/
    val label: String? = null,
) : NavKey {

    val showLabel: String
        get() = label ?: name ?: path

}

/**默认的导航动画时长
 * - [defaultTransitionSpec]
 * - [defaultPopTransitionSpec]
 * - [defaultPredictivePopTransitionSpec]
 * */
const val DEFAULT_TRANSITION_DURATION_MILLISECOND = 300


/**App导航回退栈
 *
 * - [NavBackStack.add] 添加一个导航
 * - [NavBackStack.remove].[NavBackStack.removeLastOrNull] 移除最后一个导航
 * */
val LocalNavBackStack = compositionLocalOf<NavBackStack<NavKey>?> { null }

/**[NavRouter]*/
val LocalNavRouter = compositionLocalOf<NavRouter?> { null }

/**
 * 最后一次显示的屏幕
 * [ScreenRoute]*/
val LocalCurrentRoute = compositionLocalOf<ScreenRoute?> { null }


