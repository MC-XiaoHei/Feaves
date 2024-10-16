package cn.xor7.xiaohei.feaves.limit

import cn.xor7.xiaohei.feaves.data.LocationData
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocationLimitsTest {
    @Test
    fun testRemoveAllIncludes() {
        val limits = testLimits.toMutableSet()
        limits.removeAllIncludes(
            LocationData(
                y = 50.0
            )
        )
        assertTrue(limits.isEmpty())
    }

    @Test
    fun testAllIncludes() {
        val limits = testLimits.toMutableSet()
        limits.removeAllIncludes(
            LocationData(
                world = "overworld"
            )
        )
        assertEquals(1, limits.size)
    }

    private val testLimits = mutableSetOf(
        LocationLimit(
            x = 0.0,
            y = 50.0,
            z = 0.0,
            world = "overworld",
            yaw = 90.0f,
            pitch = 90.0f,
        ),
        LocationLimit(
            x = 100.0,
            y = 50.0,
            z = 100.0,
            world = "overworld",
            yaw = 90.0f,
            pitch = 90.0f,
        ),
        LocationLimit(
            x = 0.0,
            y = 50.0,
            z = 0.0,
            world = "nether",
            yaw = 90.0f,
            pitch = 90.0f,
        ),
    )
}