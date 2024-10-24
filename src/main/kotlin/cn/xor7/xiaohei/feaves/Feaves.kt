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
import org.leavesmc.leaves.event.bot.BotActionExecuteEvent
import org.leavesmc.leaves.event.bot.BotActionScheduleEvent
import org.leavesmc.leaves.event.bot.BotActionStopEvent
import org.leavesmc.leaves.event.bot.BotDeathEvent
import org.leavesmc.leaves.event.bot.BotEvent
import org.leavesmc.leaves.event.bot.BotJoinEvent
import org.leavesmc.leaves.event.bot.BotRemoveEvent
import org.leavesmc.leaves.event.bot.BotSpawnLocationEvent
import java.time.Clock
import kotlin.coroutines.CoroutineContext

typealias EventDispatcher = (event: Event) -> CoroutineContext

const val PROJECT_NAME = "Feaves"
lateinit var feavesInstance: Feaves
var pluginClock = Clock.systemUTC()

open class Feaves : SuspendingJavaPlugin() {
    private val eventDispatcher = mapOf<Class<out Event>, EventDispatcher>(
        botDispatcher<BotActionExecuteEvent>(),
        botDispatcher<BotActionScheduleEvent>(),
        botDispatcher<BotActionStopEvent>(),
        botDispatcher<BotDeathEvent>(),
        botDispatcher<BotJoinEvent>(),
        botDispatcher<BotRemoveEvent>(),
        botDispatcher<BotSpawnLocationEvent>()
    )

    override suspend fun onEnableAsync() {
        feavesInstance = this
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

    private inline fun <reified T : BotEvent> botDispatcher(): Pair<Class<out T>, EventDispatcher> =
        Pair(T::class.java) { event ->
            require(event is T)
            entityDispatcher(event.bot)
        }
}
