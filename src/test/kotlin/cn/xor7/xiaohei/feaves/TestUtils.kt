package cn.xor7.xiaohei.feaves

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import cn.xor7.xiaohei.feaves.data.BotDataManager
import com.github.shynixn.mccoroutine.folia.MCCoroutine
import com.github.shynixn.mccoroutine.folia.asyncDispatcher
import com.github.shynixn.mccoroutine.folia.test.TestMCCoroutine
import kotlinx.coroutines.runBlocking
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration

lateinit var TEST_SERVER: ServerMock

fun initMock() {
    MCCoroutine.Driver = TestMCCoroutine.Driver
    TEST_SERVER = MockBukkit.mock()
    MockBukkit.load<Feaves>(Feaves::class.java)
}

fun closeMock() {
    MockBukkit.unmock()
}

fun runInBotDataManager(block: suspend BotDataManager.() -> Unit) {
    runBlocking(feavesInstance.asyncDispatcher) {
        BotDataManager.block()
    }
}

fun setMockTime(time: Duration) {
    pluginClock = Clock.fixed(Instant.ofEpochMilli(time.inWholeMilliseconds), ZoneId.systemDefault())
}