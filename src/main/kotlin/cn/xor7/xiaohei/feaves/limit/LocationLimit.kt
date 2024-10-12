package cn.xor7.xiaohei.feaves.limit

import kotlinx.serialization.Serializable

@Serializable
data class LocationLimit(
    val name: String? = null,
    val x: Int? = null,
    val y: Int? = null,
    val z: Int? = null,
    val world: String? = null,
    val yaw: Float? = null,
    val pitch: Float? = null,
)