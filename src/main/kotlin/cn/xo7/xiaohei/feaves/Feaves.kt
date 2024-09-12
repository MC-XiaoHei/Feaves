package cn.xo7.xiaohei.feaves

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.plugin.java.JavaPlugin

var INSTANCE: Feaves? = null

class Feaves : JavaPlugin() {
    init {
        INSTANCE = this
    }

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this)
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
