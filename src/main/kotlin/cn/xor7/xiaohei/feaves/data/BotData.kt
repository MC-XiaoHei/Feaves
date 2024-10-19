package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.limit.Limits
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

typealias BotDataFileModule = MutableMap<@Contextual Uuid, BotData>

@Serializable
data class BotData(
    val name: String,
    val storage: StorageType = StorageType.BOT,
    val limits: Limits = Limits(),
)
