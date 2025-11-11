package com.angcyo.compose.core.objectbox

import androidx.annotation.Keep
import com.angcyo.compose.basics.unit.Page
import com.angcyo.compose.basics.unit.nowTime
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 *
 * 消息日志存储数据结构
 */
@Keep
@Entity
data class MessageLogEntity(
    @Id
    var entityId: Long = 0,
    /**创建时间, 13为毫秒时间戳*/
    var createTime: Long = nowTime,
    /**更新时间, 13为毫秒时间戳*/
    var updateTime: Long = nowTime,
    //--
    /**消息内容*/
    var content: String? = null,
    /**消息概述*/
    var summary: String? = null,
    /**消息描述*/
    var description: String? = null,
    /**消息备注*/
    var remark: String? = null,
) {
    companion object {

        /**保存一条记录
         * @return 数据id*/
        fun save(content: String? = null): Long {
            val entity = MessageLogEntity()
            entity.content = content
            return boxOf(MessageLogEntity::class).put(entity)
        }

        /**分页查询, 降序*/
        fun query(page: Page = Page()): List<MessageLogEntity> {
            return boxOf(MessageLogEntity::class).page(page) {
                orderDesc(MessageLogEntity_.createTime)
            }
        }
    }
}