package cn.xo7.xiaohei.feaves

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.locationArgument
import dev.jorel.commandapi.kotlindsl.rotationArgument
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.worldArgument
import dev.jorel.commandapi.wrappers.Rotation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.leavesmc.leaves.entity.Bot
import org.leavesmc.leaves.entity.botaction.BotActionType

fun registerCommands() {
    commandAPICommand("feaves", Feaves) {
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
                    val name = args[1] as String? ?: ""
                    val location = args[2] as Location?
                        ?: (sender as? Player)?.location
                        ?: bot.location
                    (args[3] as? World)?.let { location.world = it }
                    (args[4] as? Rotation)?.let { location.commandAPIRotation = it }
                    bot.addLocationLimitPoint(name, location)
                    sender.sendMessage("Add creatable point $location for bot ${bot.name}")
                }
            }
            literalArgument("perms") {

            }
            literalArgument("gui") {

            }
        }
    }
}

fun Bot.addLocationLimitPoint(name: String, location: Location) {
    TODO()
}

var Location.commandAPIRotation
    get() = Rotation(yaw, pitch)
    set(value) {
        this.yaw = value.yaw
        this.pitch = value.pitch
    }

inline fun CommandAPICommand.botArgument(
    nodeName: String,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(CustomArgument<Bot, String>(StringArgument(nodeName)) { info ->
        Bukkit.getBotManager().getBot(info.input)
            ?: throw CustomArgumentException
                .fromMessageBuilder(
                    MessageBuilder("No such bot: ")
                        .appendArgInput()
                )
    }.replaceSuggestions(ArgumentSuggestions.strings { _ ->
        Bukkit.getBotManager().bots.map { it.name }.toTypedArray()
    }).apply(block))

inline fun CommandAPICommand.actionTypeArgument(
    nodeName: String,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(CustomArgument<BotActionType, String>(StringArgument(nodeName)) { info ->
        BotActionType.entries.find { it.name.equals(info.input, true) }
            ?: throw CustomArgumentException
                .fromMessageBuilder(
                    MessageBuilder("No such action type: ")
                        .appendArgInput()
                )
    }.replaceSuggestions(ArgumentSuggestions.strings { _ ->
        BotActionType.entries.map { it.name }.toTypedArray()
    }).apply(block))

inline fun CommandAPICommand.permsNodeArgument(
    nodeName: String,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(StringArgument(nodeName).replaceSuggestions(ArgumentSuggestions.strings { _ ->
        BotActionType.entries.map { it.name }.toTypedArray()
    }).apply(block))