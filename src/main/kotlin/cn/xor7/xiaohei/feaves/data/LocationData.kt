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
    fun match(other: LocationData): Boolean = allTrue(
        name.match(other.name),
        x.match(other.x),
        y.match(other.y),
        z.match(other.z),
        world.match(other.world),
        yaw.match(other.yaw),
        pitch.match(other.pitch)
    )

    private fun <T> T.match(other: T) = this == null || this == other

    private fun allTrue(vararg conditions: Boolean): Boolean = conditions.all { it }
}