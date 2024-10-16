package cn.xor7.xiaohei.feaves.data

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val name: String? = null,
    val x: Double? = null,
    val y: Double? = null,
    val z: Double? = null,
    val world: String? = null,
    val yaw: Float? = null,
    val pitch: Float? = null,
) {
    fun includes(other: LocationData): Boolean = allTrue(
        other.name.nullOrEquals(name),
        other.x.nullOrEquals(x),
        other.y.nullOrEquals(y),
        other.z.nullOrEquals(z),
        other.world.nullOrEquals(world),
        other.yaw.nullOrEquals(yaw),
        other.pitch.nullOrEquals(pitch)
    )

    private fun <T> T.nullOrEquals(other: T) = this == null || this == other

    private fun allTrue(vararg conditions: Boolean): Boolean = conditions.all { it }
}