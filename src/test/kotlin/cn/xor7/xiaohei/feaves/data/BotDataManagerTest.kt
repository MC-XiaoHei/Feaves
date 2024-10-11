package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.INSTANCE
import cn.xor7.xiaohei.feaves.closeMock
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
            "testUUID1": {
                "storage": "BOT"
            },
            "testUUID2": {
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
            "testUUID3": {
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
    val dataFile = File(DATA_FILE).also { it.parentFile.mkdirs() }

    @BeforeEach
    fun init() {
        initMock()
        dataFile.createNewFile()
        dataFile.writeText(TEST_DATA_FILE_CONTENT)
    }

    @BeforeEach
    fun close() {
        closeMock()
    }

    @AfterEach
    fun clean() {
        dataFile.delete()
    }

    @Test
    fun testLoadBotStorageType() = run {
        val botData1 = getBotData("testUUID1")
        assertEquals(StorageType.BOT, botData1.storage)

        val botData2 = getBotData("testUUID2")
        assertEquals(StorageType.PLAYER, botData2.storage)
    }

    @Test
    fun testLoadBotActionLimit() = run {
        val actionLimit = getBotData("testUUID3").limit.action
        assertEquals(2, actionLimit.size)

        assertEquals(
            ActionLimitData(
                enable = false
            ),
            actionLimit["*"]
        )

        assertEquals(
            ActionLimitData(
                enable = true
            ),
            actionLimit["use"]
        )
    }

    @Test
    fun testLoadBotLocationLimit() = run {
        val locations = getBotData("testUUID2").limit.create.locations
        assertEquals(2, locations.size)

        assertContains(
            locations,
            LocationData(
                x = 0, y = 0, z = 0, world = "overworld"
            )
        )

        assertContains(
            locations,
            LocationData(
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