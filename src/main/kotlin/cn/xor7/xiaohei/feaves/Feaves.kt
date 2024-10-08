package cn.xor7.xiaohei.feaves

import cn.xor7.xiaohei.feaves.command.registerCommands
import cn.xor7.xiaohei.feaves.listener.BotListener
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

const val PROJECT_NAME = "Feaves"
typealias EventDispatcher = (event: Event) -> CoroutineContext

lateinit var INSTANCE: Feaves

open class Feaves : SuspendingJavaPlugin() {
    private val eventDispatcher = mapOf<Class<out Event>, EventDispatcher>(
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
        INSTANCE = this
        CommandAPI.onEnable()
        Bukkit.getPluginManager().registerSuspendingEvents(
            BotListener,
            this,
            eventDispatcher
        )
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
