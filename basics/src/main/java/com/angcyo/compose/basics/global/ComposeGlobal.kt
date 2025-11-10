package com.angcyo.compose.basics.global

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.autofill.AutofillManager
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.GraphicsContext
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.input.InputModeManager
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalCursorBlinkEnabled
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.platform.LocalScrollCaptureInProgress
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.PlatformTextInputModifierNode
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.platform.WindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/08
 *
 * Compose 全局实例
 *
 * # 全局实例
 *
 * - [LocalContext] 获取[Context]
 * - [LocalConfiguration] 获取[Configuration]
 * - [LocalViewConfiguration] 获取[ViewConfiguration]
 * - [LocalWindowInfo] 获取[WindowInfo]
 * - [LocalLifecycleOwner] 获取[LifecycleOwner]
 * - [LocalView] 获取[View]
 * - [LocalSavedStateRegistryOwner] 获取[SavedStateRegistryOwner]
 * - [LocalViewModelStoreOwner] 获取[ViewModelStoreOwner]
 *
 * - [LocalClipboard] 获取[Clipboard]
 * - [LocalAccessibilityManager] 获取[AccessibilityManager]
 * - [LocalAutofillManager] 获取[AutofillManager]
 * - [LocalHapticFeedback] 获取[HapticFeedback]
 * - [LocalSoftwareKeyboardController] 获取[SoftwareKeyboardController]
 * - [LocalInputModeManager] 获取[InputModeManager]
 *
 * - [LocalTextToolbar] 获取[TextToolbar]
 * - [LocalFocusManager] 获取[FocusManager]
 * - [LocalDensity] 获取[Density]
 * - [LocalGraphicsContext] 获取[GraphicsContext]
 * - [LocalLayoutDirection] 获取[LayoutDirection]
 * - [LocalUriHandler] 获取[UriHandler]
 *
 * - [LocalScrollCaptureInProgress] 获取[Boolean]
 * - [LocalCursorBlinkEnabled] 获取[Boolean]
 * - [LocalInspectionMode] 获取[Boolean]
 *
 * # 全局扩展方法
 *
 * - [findViewTreeViewModelStoreOwner]
 *
 * # 自定义 [CompositionLocal]
 *
 * 使用 CompositionLocal 将数据的作用域限定在局部
 * https://developer.android.com/develop/ui/compose/compositionlocal?hl=zh-cn
 *
 * ```
 * // 创建
 * val LocalMyService = staticCompositionLocalOf<MyService> { error("No MyService provided") }
 * 或
 * val LocalMyService = compositionLocalOf<MyService?> { null }
 *
 * // 提供值
 * CompositionLocalProvider(LocalMyService provides myService) { /* subtree */ }
 *
 * // 使用
 * val myService = LocalMyService.current
 * ```
 *
 * - [CompositionLocalProvider]
 *
 */
class ComposeGlobal {

    init {
        //PlatformTextInputModifierNode
        //CompositionLocal


    }
}