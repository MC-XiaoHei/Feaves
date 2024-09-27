package cn.xo7.xiaohei.feaves.data

import kotlinx.serialization.Serializable

typealias UUIDString = String

@Serializable
data class FileModule(
    val bots: MutableMap<UUIDString, BotData> = mutableMapOf(),
)

@Serializable
data class BotData(
    val storage: StorageType = StorageType.BOT,
    val limit: LimitData = LimitData(),
)

@Serializable
data class LimitData(
    val create: CreateLimitData = CreateLimitData(),
)

@Serializable
data class CreateLimitData(
    val location: LocationLimitData = LocationLimitData(),
)

@Serializable
data class LocationLimitData(
    val locations: MutableSet<LocationData> = mutableSetOf(),
)

@Serializable
data class LocationData(
    val name: String? = null,
    val x: Int,
    val y: Int,
    val z: Int,
    val world: String,
    val yaw: Float? = null,
    val pitch: Float? = null,
)

@Serializable
enum class StorageType {
    PLAYER,
    BOT,
}