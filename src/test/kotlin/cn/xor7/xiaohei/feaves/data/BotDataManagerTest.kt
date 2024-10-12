package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.INSTANCE
import cn.xor7.xiaohei.feaves.closeMock
import cn.xor7.xiaohei.feaves.data.BotDataManager.DATA_FILE
import cn.xor7.xiaohei.feaves.limit.LocationLimit
import cn.xor7.xiaohei.feaves.limit.ActionLimit
import cn.xor7.xiaohei.feaves.initMock
import com.github.shynixn.mccoroutine.folia.asyncDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.File
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

const val TEST_DATA_FILE_CONTENT = """
        {
            "botWithNothing": {
                "storage": "BOT"
            },
            "playerWithCreateLocationLimit": {
                "storage": "PLAYER",
                "limit": {
                    "create": {
                        "locations": [
                            {
                                "x": 0,
                                "y": 0,
                                "z": 0,
                                "world": "overworld"
                            },{
                                "x": 10,
                                "y": 10,
                                "z": 10,
                                "world": "nether"
                            }
                        ]
                    }
                }
            },
            "playerWithActionLimit": {
                "storage": "PLAYER"
                "limit": {
                    "action": {
                        "*": {
                            "enable": false
                        },
                        "use": {
                            "enable": true
                        }
                    }
                }
            }
        }
    """

class BotDataManagerTest {
    init {
        File(DATA_FILE).also {
            it.parentFile.mkdirs()
            if (it.exists()) {
                it.delete()
            }
            it.createNewFile()
            it.writeText(TEST_DATA_FILE_CONTENT)
        }
    }

    @BeforeEach
    fun init() = initMock()

    @AfterEach
    fun clean() = closeMock()

    @Test
    fun testLoadBotStorageType() = run {
        val botData1 = copyBotData("botWithNothing")
        assertEquals(StorageType.BOT, botData1.storage)

        val botData2 = copyBotData("playerWithCreateLocationLimit")
        assertEquals(StorageType.PLAYER, botData2.storage)
    }

    @Test
    fun testLoadBotActionLimit() = run {
        val actionLimit = copyBotData("playerWithActionLimit").limit.action
        assertEquals(2, actionLimit.size)

        assertEquals(
            ActionLimit(
                enable = false
            ),
            actionLimit["*"]
        )

        assertEquals(
            ActionLimit(
                enable = true
            ),
            actionLimit["use"]
        )
    }

    @Test
    fun testLoadBotLocationLimit() = run {
        val locations = copyBotData("playerWithCreateLocationLimit")
            .limit.create.locations
        assertEquals(2, locations.size)

        assertContains(
            locations,
            LocationLimit(
                x = 0, y = 0, z = 0, world = "overworld"
            )
        )

        assertContains(
            locations,
            LocationLimit(
                x = 10, y = 10, z = 10, world = "nether"
            )
        )
    }

    fun run(block: suspend BotDataManager.() -> Unit) {
        runBlocking(INSTANCE.asyncDispatcher) {
            BotDataManager.block()
        }
    }
}