package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.closeMock
import cn.xor7.xiaohei.feaves.initBotDataManagerWith
import cn.xor7.xiaohei.feaves.initMock
import cn.xor7.xiaohei.feaves.limit.ActionLimit
import cn.xor7.xiaohei.feaves.limit.LocationLimit
import cn.xor7.xiaohei.feaves.runInBotDataManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.util.NoSuchElementException
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
            "limits": {
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
        }
    }
    """

class BotDataManagerTest {
    @BeforeEach
    fun init() {
        initMock()
        initBotDataManagerWith(TEST_DATA_FILE_CONTENT)
    }

    @AfterEach
    fun clean() = closeMock()

    @Test
    fun testLoadBotStorageType() = runInBotDataManager {
        val botData1 = copyBotData("botWithNothing")
        assertEquals(StorageType.BOT, botData1.storage)

        val botData2 = copyBotData("playerWithCreateLocationLimit")
        assertEquals(StorageType.PLAYER, botData2.storage)
    }

    @Test
    fun testLoadBotActionLimit() = runInBotDataManager {
        val actionLimit = copyBotData("playerWithActionLimit").limits.actions
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
    fun testLoadBotLocationLimit() = runInBotDataManager {
        val locations = copyBotData("playerWithCreateLocationLimit").limits.create.locations
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

    @Test
    fun testCreateDataFileAndCopyDataOfNotExistBot() = runInBotDataManager {
        val file = File(DATA_FILE)
        if (file.exists()) {
            file.delete()
        }
        BotDataManager.init()
        assertThrows<NoSuchElementException> {
            copyBotData("notExistBot")
        }
        assert(file.isFile)
    }
}