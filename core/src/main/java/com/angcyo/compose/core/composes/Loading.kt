package com.angcyo.compose.core.composes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.angcyo.compose.core.modifier.it

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/118
 *
 * - [CircularProgressIndicator]
 * - [LinearProgressIndicator]
 */

/**全屏加载指示器
 * - [size].[width].[height] 指定显示的大小, 否则全屏
 * - [onFirstVisible] 第一次可见时触发的回调
 * - [indicator] 指示器, 不指定则使用[CircularProgressIndicator]
 * */
@Preview
@Composable
fun FullscreenLoading(
    modifier: Modifier = Modifier,
    size: Dp? = null, width: Dp? = null, height: Dp? = null,
    //--
    onFirstVisible: () -> Unit = {},
    //--
    indicator: @Composable (BoxScope.() -> Unit)? = null,
) {
    val w = size ?: width
    val h = size ?: height
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onFirstVisible(minDurationMs = 160, minFractionVisible = 0f) {
                onFirstVisible()
            }
            /*.onVisibilityChanged()*/
            .it(w == null && h == null) { fillMaxSize() }
            .it(w != null && h != null) {
                size(
                    w ?: Dp.Unspecified,
                    h ?: Dp.Unspecified
                )
            }
            .it(w == null && h != null) {
                size(w ?: Dp.Unspecified, h ?: Dp.Unspecified).fillMaxWidth()
            }
            .it(w != null && h == null) {
                size(w ?: Dp.Unspecified, h ?: Dp.Unspecified).fillMaxHeight()
            },
    ) {
        indicator?.invoke(this) ?: CircularProgressIndicator()
    }
}

/**
 * [LazyColumn]底部加载更多视图
 * */
@Preview
@Composable
fun LastLoadMoreItem(
    haveLoadMore: Boolean = true,
    noMoreIndicator: @Composable (BoxScope.() -> Unit)? = null,
    loadMoreIndicator: @Composable (BoxScope.() -> Unit)? = null,
    onLoadMore: () -> Unit = {}
) {
    FullscreenLoading(
        height = 50.dp,
        onFirstVisible = onLoadMore,
        indicator = if (haveLoadMore) loadMoreIndicator else noMoreIndicator ?: {
            Text("~没有更多了~")
        }
    )
}