package com.angcyo.compose.core.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.angcyo.compose.core.nav.LocalCurrentRoute

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/10
 *
 * 一屏/屏幕显示的一些基础封装
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
    modifier: Modifier = Modifier,
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
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(
        LocalSnackbar provides snackbarHostState,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = topBar ?: {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(title ?: LocalCurrentRoute.current?.showLabel ?: "")
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
    modifier: Modifier = Modifier,
    //--
    topBar: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    //--TopAppBar
    title: String? = null,
    //--
    content: LazyListScope.() -> Unit,
) {
    ScaffoldScreen(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        title = title,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}
