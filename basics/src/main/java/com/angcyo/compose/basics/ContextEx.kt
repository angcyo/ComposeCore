package com.angcyo.compose.basics

import android.content.Context
import android.widget.Toast

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/08
 */

/**使用Android原生, 弹出[toast]提示*/
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}