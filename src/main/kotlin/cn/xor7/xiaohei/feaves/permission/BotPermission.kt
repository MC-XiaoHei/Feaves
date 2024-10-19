package cn.xor7.xiaohei.feaves.permission

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import kotlin.uuid.Uuid

data class BotPermission(val botUuid: Uuid, val perm: String) {
    val permissionNode: String
        get() = "feaves.bot.$botUuid.$perm"

    val permission: Permission
        get() {
            return Bukkit.getPluginManager().getPermission(permissionNode) ?: run {
                val permission = Permission(permissionNode)
                Bukkit.getPluginManager().addPermission(permission)
                permission
            }
        }
}