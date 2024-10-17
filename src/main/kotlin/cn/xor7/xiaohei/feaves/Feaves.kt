package cn.xor7.xiaohei.feaves

import cn.xor7.xiaohei.feaves.command.registerCommands
import cn.xor7.xiaohei.feaves.listener.BotListener
import cn.xor7.xiaohei.feaves.luckperms.detectLuckPerms
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.registerSuspendingEvents
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.leavesmc.leaves.event.bot.BotActionScheduleEvent
import org.leavesmc.leaves.event.bot.BotActionStopEvent
import java.time.Clock
import kotlin.coroutines.CoroutineContext

typealias EventDispatcher = (event: Event) -> CoroutineContext

const val PROJECT_NAME = "Feaves"
lateinit var feavesInstance: Feaves
var pluginClock = Clock.systemUTC()

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
        feavesInstance = this
        detectLuckPerms()
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
