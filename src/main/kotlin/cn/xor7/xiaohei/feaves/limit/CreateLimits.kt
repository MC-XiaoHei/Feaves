package cn.xor7.xiaohei.feaves.limit

import kotlinx.serialization.Serializable

@Serializable
data class CreateLimits(
    val locations: LocationLimits = mutableSetOf(),
)