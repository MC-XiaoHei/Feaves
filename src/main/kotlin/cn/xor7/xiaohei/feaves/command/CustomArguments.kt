package cn.xor7.xiaohei.feaves.command

import cn.xor7.xiaohei.feaves.action.Action
import cn.xor7.xiaohei.feaves.action.actions
import cn.xor7.xiaohei.feaves.data.BotData
import cn.xor7.xiaohei.feaves.data.BotDataManager
import cn.xor7.xiaohei.feaves.feavesInstance
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.StringArgument
import kotlinx.coroutines.runBlocking

inline fun CommandAPICommand.botArgument(
    nodeName: String,
    block: Argument<*>.() -> Unit = {},
): CommandAPICommand =
    withArguments(CustomArgument<BotData, String>(StringArgument(nodeName)) { info ->
        runBlocking(feavesInstance.globalRegionDispatcher) {
            try {
                return@runBlocking BotDataManager.run {
                    return@run getBotData(info.input.botUuid)
                }
            } catch (_: NoSuchElementException) {
                throw CustomArgumentException.fromString("No such bot: ${info.input}")
            }
        }
    }.replaceSuggestions(ArgumentSuggestions.strings { _ ->
        runBlocking(feavesInstance.globalRegionDispatcher) {
            BotDataManager.botNames().toTypedArray()
        }
    }).apply(block))

inline fun CommandAPICommand.actionTypeArgument(
    nodeName: String,
    block: Argument<*>.() -> Unit = {},
): CommandAPICommand =
    withArguments(CustomArgument<Action, String>(StringArgument(nodeName)) { info ->
        try {
            Action.fromDisplayName(info.input)
        } catch (_: IllegalArgumentException) {
            throw CustomArgumentException.fromString("No such action: ${info.input}")
        }
    }.replaceSuggestions(ArgumentSuggestions.strings { _ ->
        actions.map { it.displayName }.toTypedArray()
    }).apply(block))

