package com.angcyo.compose.core.modifier

import androidx.compose.foundation.border
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/10
 */

/**显示元素的边界*/
@Stable
fun Modifier.bounds(
    width: Dp = 1.dp,
    brush: Brush = SolidColor(Color.Magenta),
    shape: Shape = RectangleShape
) = border(width, brush, shape)