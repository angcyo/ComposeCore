package com.angcyo.compose.basics.hawk

import androidx.annotation.Keep
import com.angcyo.compose.basics.unit.getAppString
import com.angcyo.library.component.hawk.HawkPropertyValue

/**
 * 内部库中的一些持久化数据
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @since 2022/10/18
 */

@Keep
object LibHawkKeys {

    /**合规key, 用来保存当前版本的合规状态*/
    const val KEY_COMPLIANCE_STATE = "KEY_COMPLIANCE_STATE"

    /**Hawk Key
     * [com.angcyo.library.getAppVersionCode]*/
    var KEY_COMPLIANCE = "${KEY_COMPLIANCE_STATE}_${getAppString("versionCode")}"

    /**是否合规了, 持久化*/
    val isCompliance: Boolean
        get() = KEY_COMPLIANCE.hawkGet() == "true"

    /**[com.angcyo.component.luban.DslLuban]
     * 压缩时, 最小的压缩像素大小 [kb]
     * */
    var minKeepSize: Int by HawkPropertyValue<Any, Int>(400)

    /**最小界面更新post延迟操作*/
    var minInvalidateDelay: Long by HawkPropertyValue<Any, Long>(16)

    /**最小post延迟操作*/
    var minPostDelay: Long by HawkPropertyValue<Any, Long>(160)

    /**日志单文件最大数据量的大小
     * 允许写入单个文件的最大大小10mb, 之后会重写*/
    var logFileMaxSize: Long by HawkPropertyValue<Any, Long>(2 * 1024 * 1024)

    /**2个浮点比较, 误差小于此值视为相等
     * 0.0000000 //浮点小数点后面有7位*/
    var floatAcceptableError: Float by HawkPropertyValue<Any, Float>(0.00001f) //5位

    /**2个双精度浮点比较, 误差小于此值视为相等
     * 0.0000000000000000 //双精度浮点小数点后面有16位*/
    var doubleAcceptableError: Double by HawkPropertyValue<Any, Double>(0.000000000000001) //15位
}