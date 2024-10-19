package cn.xor7.xiaohei.feaves.permission.luckperms

import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit

lateinit var luckPermsInstance: LuckPerms
var luckPermsAvailable = false

fun detectLuckPerms() = try {
    val provider = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)
    if (provider != null) {
        luckPermsInstance = provider.provider
        luckPermsAvailable = true
    } else {
        luckPermsAvailable = false
    }
} catch (_: NoClassDefFoundError) {
    luckPermsAvailable = false
}