package com.angcyo.compose.basics.hawk

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.angcyo.compose.basics.annotation.Initial
import com.angcyo.compose.basics.annotation.Output
import com.angcyo.compose.basics.hawk.RHawk.HAWK_SPLIT_CHAR
import com.angcyo.compose.basics.unit.L
import com.angcyo.compose.basics.unit.connect
import com.angcyo.compose.basics.unit.splitList
import com.orhanobut.hawk.Hawk
import java.io.File

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 *
 * https://github.com/orhanobut/hawk
 */
object RHawk {

    const val HAWK_SPLIT_CHAR = "|"

    /**[Hawk]文件存储的路径*/
    @Output
    var hawkPath: String? = null

    /**初始化库*/
    @Initial
    fun initHawk(context: Context?) {
        if (context == null) {
            return
        }
        /*sp持久化库*/
        fun initHawkInner() {
            Hawk.init(context).build()
            val path = "/shared_prefs/Hawk2.xml"
            val hawkXmlFile = File(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.dataDir
                } else {
                    context.filesDir.parentFile
                }, path
            )
            if (hawkXmlFile.exists()) {
                hawkPath = hawkXmlFile.absolutePath
            }
            L.i("初始化Hawk -> $hawkPath")
        }
        try {
            if (!Hawk.isBuilt()) {
                initHawkInner()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            initHawkInner()
        }
    }
}


/**指定的key是否存在*/
fun String?.hawkHave(): Boolean = hawkContains()

/**指定的key是否存在*/
fun String?.hawkContains(): Boolean {
    return this?.run {
        Hawk.contains(this)
    } ?: false
}

/**删除指定的key*/
fun String?.hawkDelete(): Boolean {
    return this?.run {
        Hawk.delete(this)
    } ?: false
}

/**
 * this 对应的 key值,
 * 将获取到的 value 用 `,` 分割, 返回列表集合 (不含空字符)
 * @param maxCount 需要返回多少条
 * */
fun String?.hawkGetList(def: String? = "", maxCount: Int = Int.MAX_VALUE): List<String> {
    val result = mutableListOf<String>()
    this?.let {
        val get = Hawk.get(it, def)

        if (!TextUtils.isEmpty(get)) {
            result.addAll(
                get.splitList(
                    HAWK_SPLIT_CHAR,
                    false,
                    true,
                    maxCount
                )
            )
        }
    }
    return result
}

/**
 * 将 value, 追加到 原来的value中
 *
 * @param sort 默认排序, 最后追加的在首位显示
 *
 * */
fun String?.hawkPutList(value: CharSequence?, sort: Boolean = true): Boolean {
    if (value.isNullOrEmpty()) {
        return false
    }
    val char = HAWK_SPLIT_CHAR
    return this?.run {
        val oldString = Hawk.get(this, "")
        val oldList = oldString.splitList(char)

        if (!sort) {
            if (oldList.contains(value)) {
                //已存在
                return true
            }
        }

        oldList.remove(value)/*最新的在前面*/
        oldList.add(0, value.toString())
        Hawk.put(this, oldList.connect(char))
    } ?: false
}

/**直接替换整个[value]*/
fun String?.hawkPutList(
    value: List<CharSequence>?, sort: Boolean = true, allowEmpty: Boolean = true
): Boolean {
    this ?: return false
    var result = false
    Hawk.put(this, "")//先清空
    if (value.isNullOrEmpty() && allowEmpty) {
        result = true
    } else {
        value?.forEach {
            //挨个追加
            result = hawkPutList(it, sort) == true || result
        }
    }
    return result
}

fun String?.hawkPut(value: CharSequence?): Boolean {
    return this?.run {
        if (value == null) {
            Hawk.delete(this)
        } else {
            Hawk.put(this, value)
        }
    } ?: false
}

/**
 *  var cacheLoginBean: LoginBean?
 *    get() = KEY_LOGIN_BEAN.hawkGet<LoginBean>()
 *    set(value) {
 *      KEY_LOGIN_BEAN.hawkPut(value)
 *   }
 * */
fun <T> String?.hawkPut(value: T?): Boolean {
    return this?.run {
        Hawk.put(this, value)
    } ?: false
}

/**如果key对应的值不存在或者为null时, 则设置值*/
fun <T> String?.hawkPutIfNull(value: T?): Boolean {
    return this?.run {
        val key = this
        val old: T? = Hawk.get(key)
        if (old == null) {
            Hawk.put(key, value)
        }
        true
    } ?: false
}

/**数字累加计算,保存并返回*/
fun String?.hawkAccumulate(value: Long = 1, def: Long = 0): Long {
    val origin = hawkGet(null)?.toLongOrNull() ?: def
    val newValue = origin + value
    hawkPut("$newValue")
    return newValue
}

/**直接追加到原来的数据尾部*/
fun String?.hawkAppend(value: CharSequence?, symbol: String = ""): Boolean {
    return this?.run {
        Hawk.put(this, "${hawkGet() ?: ""}${symbol}${value ?: ""}")
    } ?: false
}

fun String?.hawkGet(def: String? = null): String? {
    var result: String? = null
    this?.let {
        try {
            result = Hawk.get<String>(it, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

/**
 * KEY_LOGIN_BEAN.hawkGet<LoginBean>()
 * */
fun <T> String?.hawkGet(def: T? = null): T? {
    var result: T? = def
    this?.let {
        try {
            result = Hawk.get<T>(it, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

fun String?.hawkGetBoolean(def: Boolean = false): Boolean {
    var result: Boolean = def
    this?.let {
        try {
            result = Hawk.get(it, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

fun String?.hawkGetInt(def: Int = -1): Int {
    var result = def
    this?.let {
        try {
            result = Hawk.get(it, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

fun String?.hawkGetLong(def: Long = -1): Long {
    var result = def
    this?.let {
        try {
            result = Hawk.get(it, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

fun String?.hawkGetFloat(def: Float = -1f): Float {
    var result = def
    this?.let {
        try {
            result = Hawk.get(it, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

fun String?.hawkGetString(def: String? = null): String? {
    var result = def
    this?.let {
        try {
            result = Hawk.get<String>(it, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

/**当前字符串key, 是否在N天内不提示
 * [isDayNotPrompt]
 * [dayNotPrompt]
 * @return 返回值表示是否不要提示*/
fun String?.isDayNotPrompt(day: Int): Boolean {
    val startTime = hawkGetLong(0)
    return if (startTime <= 0L) {
        //首次
        false
    } else {
        val now = System.currentTimeMillis()
        //是否在N天内
        now < startTime + day * 24 * 3600 * 1000
    }
}

/**
 * 设置当前字符串N天内不提示
 * [isDayNotPrompt]
 * [dayNotPrompt]
 * */
fun String?.dayNotPrompt(enable: Boolean = true) {
    if (enable) {
        hawkPut(System.currentTimeMillis())
    } else {
        hawkDelete()
    }
}