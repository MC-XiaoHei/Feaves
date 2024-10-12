package cn.xor7.xiaohei.feaves.limit

import kotlinx.serialization.Serializable

@Serializable
data class ActionLimit(
    val enable: Boolean? = null,
    val cooldown: String = "0",
    val lastUseMillions: Long = 0
)