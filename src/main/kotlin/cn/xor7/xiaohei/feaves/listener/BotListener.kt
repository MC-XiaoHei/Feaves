package cn.xor7.xiaohei.feaves.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.leavesmc.leaves.event.bot.BotActionScheduleEvent
import org.leavesmc.leaves.event.bot.BotConfigModifyEvent
import org.leavesmc.leaves.event.bot.BotCreateEvent
import org.leavesmc.leaves.event.bot.BotRemoveEvent


object BotListener : Listener {
    @EventHandler
    fun onBotActionSchedule(e: BotActionScheduleEvent) {
    }

    @EventHandler
    fun onBotConfigModify(e: BotConfigModifyEvent) {

    }

    @EventHandler
    fun onBotRemove(e: BotRemoveEvent) {

    }

    @EventHandler
    fun onBotCreate(e: BotCreateEvent) {
    }
}