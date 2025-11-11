package com.angcyo.compose.core.objectbox

import androidx.annotation.Keep
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 *
 * https://docs.objectbox.io/getting-started#kotlin
 *
 * # Generate ObjectBox code
 * https://docs.objectbox.io/getting-started#generate-objectbox-code
 *
 * 会生成对应的模型文件 `xxx/objectbox-models/default.json`
 */
@Keep
@Entity
data class PlaceholderEntity(
    @Id
    var entityId: Long = 0,
    var name: String? = null

    /**[io.objectbox.annotation.Transient]*/
    /**[io.objectbox.annotation.NameInDb]*/
    /**[io.objectbox.annotation.Uid]*/
    /**[io.objectbox.annotation.Index]*/
    /**[io.objectbox.relation.ToOne]*/

    /**[io.objectbox.annotation.Backlink]*/
    /**[io.objectbox.relation.ToMany]*/
)

@Target(AnnotationTarget.FUNCTION)
annotation class OperateEntity(val des: String = "增删改")