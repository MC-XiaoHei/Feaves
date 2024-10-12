package cn.xor7.xiaohei.feaves.limit

import kotlinx.serialization.Serializable

typealias LocationLimits = MutableSet<LocationLimit>

@Serializable
data class CreateLimits(
    val locations: LocationLimits = mutableSetOf(),
)