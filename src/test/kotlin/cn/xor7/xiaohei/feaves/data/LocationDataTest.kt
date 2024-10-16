package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.closeMock
import cn.xor7.xiaohei.feaves.initMock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class LocationDataTest {
    @BeforeEach
    fun init() = initMock()

    @AfterEach
    fun clean() = closeMock()

    @Test
    fun testIncludes() {
        val real = LocationData(
            name = "test",
            world = "world",
            x = 0.10,
            y = 0.10,
            z = 0.10,
            yaw = 0.10F,
            pitch = 0.10F
        )
        setOf(
            LocationData(name = "test"),
            LocationData(world = "world"),
            LocationData(x = 0.10),
            LocationData(y = 0.10),
            LocationData(z = 0.10),
            LocationData(yaw = 0.10F),
            LocationData(pitch = 0.10F),
            LocationData(name = "test", world = "world"),
            LocationData(name = "test", x = 0.10),
            LocationData(x = 0.10, y = 0.10),
        ).forEach { limit ->
            assertTrue(real.includes(limit))
        }
    }
}