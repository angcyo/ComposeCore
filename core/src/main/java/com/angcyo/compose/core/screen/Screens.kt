package com.angcyo.compose.core.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.angcyo.compose.core.nav.LocalCurrentRoute
import com.angcyo.compose.core.nav.LocalNavRouter

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/10
 *
 * 一屏/屏幕显示的一些基础封装
 *
 * # 安全区域
 * - [Modifier.statusBarsPadding] 状态栏
 * - [Modifier.navigationBarsPadding] 导航栏
 * - [Modifier.systemBarsPadding] 状态栏和导航栏
 *
 * - [Modifier.safeContentPadding] 安全内容区域填充
 * - [Modifier.safeDrawingPadding] 安全绘制区域填充
 * - [Modifier.imePadding] 输入法区域填充
 */

/**
 * # Scaffold 脚手架
 * https://developer.android.com/develop/ui/compose/components/scaffold?hl=zh-cn
 *
 * - [Surface]
 * - [ModalBottomSheet]
 *
 * ## 顶部应用栏
 * https://developer.android.google.cn/develop/ui/compose/components/app-bars?hl=zh-cn
 * - [TopAppBar]
 * - [CenterAlignedTopAppBar]
 * - [MediumTopAppBar]
 * - [LargeTopAppBar]
 *
 * ## 底部应用栏
 * https://developer.android.google.cn/develop/ui/compose/components/app-bars?hl=zh-cn#bottom
 * - [BottomAppBar]
 * - [FloatingActionButton]
 *
 * - [rememberCoroutineScope]
 *
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen(
    scaffoldModifier: Modifier = Modifier,
    //--
    topBar: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    //--TopAppBar
    title: String? = null,
    //--
    content: @Composable (PaddingValues) -> Unit
) {
    val router = LocalNavRouter.current
    val snackbarHostState = remember { SnackbarHostState() }
    //val ripple = rememberRipple(bounded = true)
    CompositionLocalProvider(
        LocalSnackbar provides snackbarHostState,
    ) {
        Scaffold(
            modifier = scaffoldModifier,
            topBar = topBar ?: {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(title ?: LocalCurrentRoute.current?.showLabel ?: "")
                    },
                    navigationIcon = {
                        if (router?.isFirstRoute() == true) {
                            //main
                        } else {
                            //back
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        router?.pop()
                                    }
                                    .padding(12.dp)
                                    .size(24.dp)
                            )
                        }
                    },
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                )
            },
            bottomBar = bottomBar ?: {},
            floatingActionButton = floatingActionButton ?: {},
            snackbarHost = snackbarHost ?: { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            if (topBar == null) {
                //default
                Box(modifier = Modifier.padding(PaddingValues(top = innerPadding.calculateTopPadding()))) {
                    content(
                        PaddingValues(
                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            top = 0.dp /*innerPadding.calculateTopPadding()*/,
                            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    )
                }
            } else {
                content(innerPadding)
            }
        }
    }
    //Scaffold(content = content)
}

/**
 * [SnackbarHostState]
 * [SnackbarHostState.showSnackbar]
 * */
val LocalSnackbar = compositionLocalOf<SnackbarHostState?> { null }

/**使用[LazyColumn]打底的脚手架
 *
 * https://developer.android.google.cn/develop/ui/compose/lists?hl=zh-cn
 *
 * - [LazyColumn]
 * - [LazyRow]
 * - [LazyVerticalGrid]
 * - [LazyHorizontalGrid]
 * - [LazyVerticalStaggeredGrid]
 * - [LazyHorizontalStaggeredGrid]
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldListScreen(
    scaffoldModifier: Modifier = Modifier,
    //--
    topBar: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    //--TopAppBar
    title: String? = null,
    //--PullToRefreshBox
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    refreshModifier: Modifier = Modifier,
    //--
    contentRefreshState: State<Int>? = null,
    content: LazyListScope.(refreshCount: Int) -> Unit,
) {
    var contentRefresh by remember { mutableIntStateOf(0) }
    val body = @Composable {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            content(contentRefreshState?.value ?: -1)
            //content(contentRefresh)
        }
    }
    //--
    ScaffoldScreen(
        scaffoldModifier = scaffoldModifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        title = title,
    ) {
        if (onRefresh == null) {
            body()
        } else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = refreshModifier
            ) {
                body()
            }
        }
    }
}
