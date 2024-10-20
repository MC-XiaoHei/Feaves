package cn.xor7.xiaohei.feaves.command

import cn.xor7.xiaohei.feaves.data.BotData
import cn.xor7.xiaohei.feaves.data.BotDataManager
import cn.xor7.xiaohei.feaves.feavesInstance
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.StringArgument
import kotlinx.coroutines.runBlocking
import org.leavesmc.leaves.entity.botaction.BotActionType

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
                throw CustomArgumentException.fromMessageBuilder(
                    MessageBuilder("No such bot: ").appendArgInput()
                )
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

