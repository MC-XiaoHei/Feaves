package cn.xor7.xiaohei.feaves

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import cn.xo7.xiaohei.feaves.Feaves
import com.github.shynixn.mccoroutine.bukkit.test.TestMCCoroutine
import com.github.shynixn.mccoroutine.folia.MCCoroutine

lateinit var TEST_SERVER: ServerMock

val TEST_INSTANCE: Feaves by lazy {
    MCCoroutine.Driver = TestMCCoroutine.Driver
    TEST_SERVER = MockBukkit.mock()
    MockBukkit.load<Feaves>(Feaves::class.java)
}