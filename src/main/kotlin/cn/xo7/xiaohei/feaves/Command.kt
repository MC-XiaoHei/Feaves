package cn.xo7.xiaohei.feaves

import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.stringArgument
import org.bukkit.command.CommandSender

fun registerCommands() {
    commandAPICommand("feaves", Feaves) {
        literalArgument("bot") {
            stringArgument("name") {

            }.replaceSuggestions(ArgumentSuggestions.strings<CommandSender>())
        }
    }
}