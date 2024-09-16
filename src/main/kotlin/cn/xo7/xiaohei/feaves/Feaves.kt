package cn.xo7.xiaohei.feaves

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig

var INSTANCE: Feaves? = null

class Feaves : SuspendingJavaPlugin() {
    init {
        INSTANCE = this
    }

    override suspend fun onEnableAsync() {
    }

    override fun onLoad() {
        CommandAPI.onLoad(
            CommandAPIBukkitConfig(this)
                .silentLogs(true)
        )
    }

    override fun onEnable() {
        CommandAPI.onEnable()
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }
}
