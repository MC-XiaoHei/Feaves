package cn.xor7.xiaohei.feaves.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.StringArgument
import org.bukkit.Bukkit
import org.leavesmc.leaves.entity.Bot
import org.leavesmc.leaves.entity.botaction.BotActionType

inline fun CommandAPICommand.botArgument(
    nodeName: String,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand =
    withArguments(CustomArgument<Bot, String>(StringArgument(nodeName)) { info ->
        Bukkit.getBotManager().getBot(CustomArgument.CustomArgumentInfo.input)
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
        BotActionType.entries.find { it.name.equals(CustomArgument.CustomArgumentInfo.input, true) }
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
    withArguments(StringArgument(nodeName).replaceSuggestions(
        ArgumentSuggestions.strings { _ ->
            BotActionType.entries.map { it.name }.toTypedArray()
        }).apply(block)
    )