package cn.xor7.xiaohei.feaves.command

import cn.xor7.xiaohei.feaves.feavesInstance
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.locationArgument
import dev.jorel.commandapi.kotlindsl.rotationArgument
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.worldArgument
import dev.jorel.commandapi.wrappers.Rotation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.leavesmc.leaves.entity.Bot

fun registerCommands() {
    commandAPICommand("feaves", feavesInstance) {
        literalArgument("bot") {
            botArgument("target")
            literalArgument("limit") {
                literalArgument("create")
                literalArgument("location")
                stringArgument("name", optional = true)
                locationArgument("location", optional = true)
                worldArgument("world", optional = true)
                rotationArgument("rotation", optional = true)
                anyExecutor { sender, args ->
                    val bot = args[0] as Bot
                    val locName = args[1] as String? ?: ""
                    val location = args[2] as Location?
                        ?: (sender as? Player)?.location
                        ?: bot.location
                    (args[3] as? World)?.let { location.world = it }
                    (args[4] as? Rotation)?.let { location.commandAPIRotation = it }
                    sender.sendMessage("Add creatable point $location for bot ${bot.name}")
                }
            }
            literalArgument("transfer") {

            }
            literalArgument("perms") {

            }
            literalArgument("gui") {

            }
        }
    }
}

var Location.commandAPIRotation
    get() = Rotation(yaw, pitch)
    set(value) {
        this.yaw = value.yaw
        this.pitch = value.pitch
    }