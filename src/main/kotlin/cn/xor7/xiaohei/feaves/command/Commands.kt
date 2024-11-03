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

                }
            }
        }
    }
}