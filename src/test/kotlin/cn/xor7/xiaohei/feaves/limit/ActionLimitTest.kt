package cn.xor7.xiaohei.feaves.limit

import cn.xor7.xiaohei.feaves.closeMock
import cn.xor7.xiaohei.feaves.initBotDataManagerWith
import cn.xor7.xiaohei.feaves.initMock
import cn.xor7.xiaohei.feaves.runInBotDataManager
import cn.xor7.xiaohei.feaves.setMockTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

const val TEST_DATA_FILE_CONTENT = """
    {
        "DefaultAllowAll": {},
        "DefaultDeniedAll": {
            "limits": {
                "actions": {
                    "*": {
                        "enable": false
                    }
                }
            }
        },
        "DefaultAllowAllWithUseDenied": {
            "limits": {
                "actions": {
                    "use": {
                        "enable": false
                    }
                }
            }
        },
        "DefaultDeniedAllWithUseAllowed": {
            "limits": {
                "actions": {
                    "*": {
                        "enable": false
                    },
                    "use": {
                        "enable": true
                    }
                }
            }
        },
        "AttackCooldown10Seconds": {
            "limits": {
                "actions": {
                    "attack": {
                        "cooldown": "10S"
                    }
                }
            }
        },
        "AttackCooldown10SecondsWithLastUseMillions": {
            "limits": {
                "actions": {
                    "attack": {
                        "cooldown": "10S",
                        "lastUseMillions": "1000"
                    }
                }
            }
        }
    }
"""

class ActionLimitTest {
    @BeforeEach
    fun init() {
        initMock()
        initBotDataManagerWith(TEST_DATA_FILE_CONTENT)
    }

    @AfterEach
    fun clean() = closeMock()

    @Test
    fun testDefaultAllowAll() = runInBotDataManager {
        val limits = getBotData("DefaultAllowAll").limits
        assertTrue(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
    }

    @Test
    fun testDefaultDeniedAll() = runInBotDataManager {
        val limits = getBotData("DefaultDeniedAll").limits
        assertFalse(limits.canUseAction("attack"))
        assertFalse(limits.canUseAction("use"))
    }

    @Test
    fun testDefaultAllowAllWithUseDenied() = runInBotDataManager {
        val limits = getBotData("DefaultAllowAllWithUseDenied").limits
        assertTrue(limits.canUseAction("attack"))
        assertFalse(limits.canUseAction("use"))
    }

    @Test
    fun testDefaultDeniedAllWithUseAllowed() = runInBotDataManager {
        val limits = getBotData("DefaultDeniedAllWithUseAllowed").limits
        assertFalse(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
    }

    @Test
    fun testAttackCooldown10Seconds() = runInBotDataManager {
        val limits = getBotData("AttackCooldown10Seconds").limits
        assertTrue(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
    }

    @Test
    fun testAttackCooldown10SecondsWithLastUseMillions() = runInBotDataManager {
        val limits = getBotData("AttackCooldown10SecondsWithLastUseMillions").limits
        setMockTime(1000.milliseconds + 10.seconds - 1.milliseconds)
        assertFalse(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
        setMockTime(1000.milliseconds + 10.seconds)
        assertTrue(limits.canUseAction("attack"))
        assertTrue(limits.canUseAction("use"))
    }
}