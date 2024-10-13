package cn.xor7.xiaohei.feaves.limit

import cn.xor7.xiaohei.feaves.CLOCK
import kotlinx.serialization.Serializable
import java.time.Duration

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

        val diffMillions = CLOCK.millis() - lastUseMillions
        return diffMillions >= Duration.parse("PT$cooldown").toMillis()
    }
}

fun Limits.canUseAction(actionName: String): Boolean {
    val actionLimit = actions[actionName] ?: return defaultCanUseAction()
    return actionLimit.canUse()
}

fun Limits.defaultCanUseAction(): Boolean {
    val defaultActionLimit = actions["*"] ?: return true
    return defaultActionLimit.canUse()
}