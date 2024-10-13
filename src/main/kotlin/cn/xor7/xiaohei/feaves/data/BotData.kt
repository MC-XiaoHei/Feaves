package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.limit.Limits
import kotlinx.serialization.Serializable

typealias UUIDString = String
typealias BotDataFileModule = MutableMap<UUIDString, BotData>

@Serializable
data class BotData(
    val storage: StorageType = StorageType.BOT,
    val limits: Limits = Limits(),
)