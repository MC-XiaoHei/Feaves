package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.closeMock
import cn.xor7.xiaohei.feaves.data.BotDataManager.DATA_FILE
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


class BotDataManagerTest {
    @BeforeEach
    fun init() {
        initMock()
        val file = File(DATA_FILE)
        file.parentFile.mkdirs()
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.writeText(TEST_DATA_FILE_CONTENT)
        BotDataManager.init()
    }

    @AfterEach
    fun clean() = closeMock()

    @Test
    fun testLoadBotStorageType() = runInBotDataManager {
        val botData1 = copyBotData(BOT_WITH_NOTHING)
        assertEquals(StorageType.BOT, botData1.storage)

        val botData2 = copyBotData(PLAYER_WITH_CREATE_LOCATION_LIMIT)
        assertEquals(StorageType.PLAYER, botData2.storage)

        val botData3 = copyBotData(NOTHING)
        assertEquals(StorageType.BOT, botData3.storage)
    }

    @Test
    fun testLoadBotActionLimit() = runInBotDataManager {
        val actionLimit = copyBotData(PLAYER_WITH_ACTION_LIMIT).limits.actions
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
        val locations = copyBotData(PLAYER_WITH_CREATE_LOCATION_LIMIT).limits.create.locations
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

    companion object {
        const val NOTHING = "Nothing"
        const val BOT_WITH_NOTHING = "BotWithNothing"
        const val PLAYER_WITH_CREATE_LOCATION_LIMIT = "PlayerWithCreateLocationLimit"
        const val PLAYER_WITH_ACTION_LIMIT = "PlayerWithActionLimit"

        const val TEST_DATA_FILE_CONTENT = """
            {
                "$NOTHING": {},
                "$BOT_WITH_NOTHING": {
                    "storage": "BOT"
                },
                "$PLAYER_WITH_CREATE_LOCATION_LIMIT": {
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
                "$PLAYER_WITH_ACTION_LIMIT": {
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
    }
}