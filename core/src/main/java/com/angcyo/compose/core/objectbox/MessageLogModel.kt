package com.angcyo.compose.core.objectbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angcyo.compose.basics.annotation.Output
import com.angcyo.compose.basics.annotation.Property
import com.angcyo.compose.basics.unit.Page
import com.angcyo.compose.basics.unit.size
import com.angcyo.compose.core.viewmodel.updateValue
import com.angcyo.compose.core.viewmodel.vmData

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/11
 *
 * 管理[MessageLogEntity]的视图模型
 * - [MessageLogEntity]
 *
 * - [viewModelScope]
 */
class MessageLogModel : ViewModel() {

    /**分页查询*/
    @Property
    val page = Page()

    /**可监听的数据集合*/
    @Output
    val messageLogData = vmData<List<MessageLogEntity>>(mutableListOf())

    /**刷新, 从第一页开始重新查询*/
    fun queryRefresh(): Boolean {
        page.pageRefresh()
        val list = MessageLogEntity.query(page)
        messageLogData.updateValue(list)
        return true
    }

    /**下一页查询*/
    fun queryLoadMore(): Boolean {
        if (page.isLoadEnd) {
            page.pageLoadMore()
            val list = MessageLogEntity.query(page)
            messageLogData.addSubValue(list, true)
            //L.i("查询到数据[$page]->${list.size}")
            if (list.size() < page.requestPageSize) {
                //no more
            } else {
                page.pageLoadEnd()
                return true
            }
        }
        return false
    }

    /**清空所有日志记录*/
    fun clearAllLog() {
        MessageLogEntity::class.clearEntity()
        queryRefresh()
    }

}