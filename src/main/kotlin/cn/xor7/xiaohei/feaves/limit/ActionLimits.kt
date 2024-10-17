package cn.xor7.xiaohei.feaves.limit

import cn.xor7.xiaohei.feaves.pluginClock
import kotlinx.serialization.Serializable
import java.time.Duration

typealias ActionLimits = MutableMap<String, ActionLimit>

@Serializable
data class ActionLimit(
    val enable: Boolean? = null,
    val cooldown: String = "0",
    val lastUseMillions: Long = 0L
) {
    fun canUse(): Boolean {
        if (enable == false) {
            return false
        }
        if (cooldown == "0" || lastUseMillions == 0L) {
            return true
        }

        val diffMillions = pluginClock.millis() - lastUseMillions
        return diffMillions >= Duration.parse("PT$cooldown").toMillis()
    }
}

fun ActionLimits.canUseAction(actionName: String): Boolean {
    val actionLimit = this[actionName] ?: return defaultCanUseAction()
    return actionLimit.canUse()
}

fun ActionLimits.defaultCanUseAction(): Boolean {
    val defaultActionLimit = this["@"] ?: return true
    return defaultActionLimit.canUse()
}