package cn.xo7.xiaohei.feaves

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.registerSuspendingEvents
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.leavesmc.leaves.event.bot.BotActionScheduleEvent
import org.leavesmc.leaves.event.bot.BotActionStopEvent
import kotlin.coroutines.CoroutineContext

object Feaves : SuspendingJavaPlugin() {
    private val eventDispatcher = mapOf<Class<out Event>, (event: Event) -> CoroutineContext>(
        Pair(BotActionScheduleEvent::class.java) {
            require(it is BotActionScheduleEvent)
            entityDispatcher(it.bot)
        },
        Pair(BotActionStopEvent::class.java) {
            require(it is BotActionStopEvent)
            entityDispatcher(it.bot)
        }
    )

    override suspend fun onEnableAsync() {
        CommandAPI.onEnable()
        Bukkit.getPluginManager().registerSuspendingEvents(BotListener, this, eventDispatcher)
        registerCommands()
    }

    override suspend fun onLoadAsync() {
        CommandAPI.onLoad(
            CommandAPIBukkitConfig(this)
                .silentLogs(true)
        )
    }

    override suspend fun onDisableAsync() {
        CommandAPI.onDisable()
    }
}
