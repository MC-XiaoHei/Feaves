package cn.xor7.xiaohei.feaves

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import com.github.shynixn.mccoroutine.folia.test.TestMCCoroutine
import com.github.shynixn.mccoroutine.folia.MCCoroutine

lateinit var TEST_SERVER: ServerMock

fun initMock() {
    MCCoroutine.Driver = TestMCCoroutine.Driver
    TEST_SERVER = MockBukkit.mock()
    MockBukkit.load<Feaves>(Feaves::class.java)
}
