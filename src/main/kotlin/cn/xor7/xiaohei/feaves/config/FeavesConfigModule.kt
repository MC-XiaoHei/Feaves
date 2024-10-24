package cn.xor7.xiaohei.feaves.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class FeavesConfigModule(
    var enablePlayerCommand: Boolean = !isLeavesServer(),
)

fun isLeavesServer(): Boolean = try {
    Class.forName("org.leavesmc.leaves.LeavesConfig")
    true
} catch (_: ClassNotFoundException) {
    false
}
