package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.TEST_INSTANCE
import com.github.shynixn.mccoroutine.folia.asyncDispatcher
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test
import kotlin.test.assertContains

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
            }
        }
    """

class BotDataManagerTest {
    @Test
    fun testLoadBotData() = runBlocking(TEST_INSTANCE.asyncDispatcher) {
        val file = File(DATA_FILE)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        try {
            file.writeText(TEST_DATA_FILE_CONTENT)
            BotDataManager.run {
                val botData1 = getBotData("testUUID1")
                assert(botData1.storage == StorageType.BOT)

                val botData2 = getBotData("testUUID2")
                assert(botData2.storage == StorageType.PLAYER)
                val locations = botData2.limit.create.locations
                assert(locations.size == 2)
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
        } finally {
            file.delete()
        }
    }
}