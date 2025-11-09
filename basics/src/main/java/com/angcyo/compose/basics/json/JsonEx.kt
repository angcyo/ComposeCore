package com.angcyo.compose.basics.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.annotations.TestOnly

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2025/11/09
 */

@Serializable
data class TestJsonBean(
    val str: String = "",
    val i: Int = 0,
    val l: Long = 0,
    val f: Float = 0f,
    val b: Boolean? = null,
)

/**
 * ```
 * val s = prettyJson.encodeToString(SceneRoute.Detail(42))
 * val obj = prettyJson.decodeFromString<SceneRoute>(s)
 * ```
 * - [Json.encodeToString]
 * - [Json.decodeFromString]
 * */
val prettyJson = Json { prettyPrint = true }

@TestOnly
fun testJson() {
    val bean = TestJsonBean()
    val json = prettyJson.encodeToString(bean)
    val bean2 = prettyJson.decodeFromString<TestJsonBean>(json)
    println(json)
}

