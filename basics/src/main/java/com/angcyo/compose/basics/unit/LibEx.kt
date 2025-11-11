package com.angcyo.compose.basics.unit

import android.graphics.Matrix
import android.net.Uri
import android.view.MotionEvent
import android.view.View

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2025/11/11
 */

/**函数别名*/
typealias Action = () -> Unit
typealias Action1 = (Any?) -> Unit
typealias Action2 = (Any?, Any?) -> Unit
typealias ClickAction = (View) -> Unit
typealias ViewAction = ClickAction
typealias TouchAction = (ev: MotionEvent) -> Boolean
typealias BooleanAction = (Boolean) -> Unit
typealias IntAction = (Int) -> Unit
typealias StringAction = (text: CharSequence?) -> Unit
typealias MatrixAction = (Matrix?) -> Unit
typealias UriAction = (uri: Uri?, exception: Exception?) -> Unit

/**别名*/
typealias ResultThrowable = (error: Throwable?) -> Unit

fun Any?.hash(): String? {
    return this?.hashCode()?.run { Integer.toHexString(this) }
}

fun Any.simpleHash(): String {
    return "${this.simpleClassName()}(${this.hash()})"
}

fun Any.simpleClassName(): String {
    if (this is Class<*>) {
        return this.simpleName
    }
    return this.javaClass.simpleName
}

fun Any.className(): String {
    if (this is Class<*>) {
        return this.name
    }
    return this.javaClass.name
}