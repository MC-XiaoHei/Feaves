package cn.xor7.xiaohei.feaves.limit

import cn.xor7.xiaohei.feaves.data.LocationData
import kotlinx.serialization.Serializable

typealias LocationLimits = MutableSet<LocationData>

@Serializable
data class CreateLimits(
    val locations: LocationLimits = mutableSetOf(),
)