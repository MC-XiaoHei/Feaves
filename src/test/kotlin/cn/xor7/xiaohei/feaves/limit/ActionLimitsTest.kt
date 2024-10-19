package cn.xor7.xiaohei.feaves.limit

import cn.xor7.xiaohei.feaves.setMockTime
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ActionLimitTest {
    @Test
    fun testDefaultAllowAll() {
        val limits = buildLimits()
        assertTrue(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
    }

    @Test
    fun testDefaultDeniedAll() {
        val limits = buildLimits(
            "@" to ActionLimit(enable = false)
        )
        assertFalse(limits.canUseAction("attack"))
        assertFalse(limits.canUseAction("use"))
    }

    @Test
    fun testDefaultAllowAllWithUseDenied() {
        val limits = buildLimits(
            "use" to ActionLimit(enable = false)
        )
        assertTrue(limits.canUseAction("attack"))
        assertFalse(limits.canUseAction("use"))
    }

    @Test
    fun testDefaultDeniedAllWithUseAllowed() {
        val limits = buildLimits(
            "@" to ActionLimit(enable = false),
            "use" to ActionLimit(enable = true)
        )
        assertFalse(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
    }

    @Test
    fun testAttackCooldown10Seconds() {
        val limits = buildLimits(
            "attack" to ActionLimit(cooldown = "10S")
        )
        assertTrue(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
    }

    @Test
    fun testAttackCooldown10SecondsWithLastUseMillions() {
        val limits = buildLimits(
            "attack" to ActionLimit(cooldown = "10S", lastUseMillions = 1000)
        )
        setMockTime(1000.milliseconds + 10.seconds - 1.milliseconds)
        assertFalse(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
        setMockTime(1000.milliseconds + 10.seconds)
        assertTrue(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
    }

    fun buildLimits(vararg actions: Pair<String, ActionLimit>) = mutableMapOf(*actions)
}