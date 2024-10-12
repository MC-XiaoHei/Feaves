package cn.xor7.xiaohei.feaves.limit

import kotlinx.serialization.Serializable

typealias ActionLimits = MutableMap<String, ActionLimit>

@Serializable
data class Limits(
    val create: CreateLimits = CreateLimits(),
    val action: ActionLimits = mutableMapOf("*" to ActionLimit()),
)