package cn.xor7.xiaohei.feaves.permission

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionDefault
import org.leavesmc.leaves.entity.Bot
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

data class BotPermission(val botUuid: Uuid, val perm: String) {
    val permissionNode: String
        get() = "feaves.bot.$botUuid.$perm"

    val permission: Permission = Bukkit.getPluginManager().getPermission(permissionNode)
        ?: Permission(permissionNode).also {
            Bukkit.getPluginManager().addPermission(it)
        }

    fun setDefault(value: PermissionDefault) = permission.setDefault(value)

    fun setDescription(value: String) = permission.setDescription(value)
}

fun Bot.botPermission(perm: String) = BotPermission(uniqueId.toKotlinUuid(), perm)

fun PermissionAttachment.setPermission(perm: BotPermission, value: Boolean) = setPermission(perm.permission, value)

fun PermissionAttachment.unsetPermission(perm: BotPermission) = unsetPermission(perm.permission)