package com.angcyo.compose.core.composes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 */

/**放在列表中的最后一个*/
fun LazyListScope.lastItem(
    key: Any? = "lastItem",
    contentType: Any? = "lastItem",
    content: @Composable (BoxScope.() -> Unit) = {},
) {
    item(key, contentType) {
        LastItem(content = content)
    }
}

/**底部过滤了安全导航区域的item*/
@Composable
fun LastItem(
    contentAlignment: Alignment = Alignment.BottomCenter,
    content: @Composable (BoxScope.() -> Unit) = {},
) {
    Box(modifier = Modifier.navigationBarsPadding(), contentAlignment = contentAlignment) {
        content()
    }
}

