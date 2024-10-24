package cn.xor7.xiaohei.feaves.listener

import cn.xor7.xiaohei.feaves.action.Action
import cn.xor7.xiaohei.feaves.action.ActionNamespace
import cn.xor7.xiaohei.feaves.data.data
import cn.xor7.xiaohei.feaves.limit.canUseAction
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.leavesmc.leaves.event.bot.BotActionExecuteEvent


object BotListener : Listener {
    @EventHandler
    suspend fun onBotActionExecute(e: BotActionExecuteEvent) = with(e) {
        isCancelled = bot.data().limits.actions.canUseAction(
            Action(ActionNamespace.LEAVES, actionName)
        )
    }
}