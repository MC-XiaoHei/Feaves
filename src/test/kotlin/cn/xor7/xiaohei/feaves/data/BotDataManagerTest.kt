package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.closeMock
import cn.xor7.xiaohei.feaves.data.BotDataManager.DATA_FILE
import cn.xor7.xiaohei.feaves.initMock
import cn.xor7.xiaohei.feaves.limit.ActionLimit
import cn.xor7.xiaohei.feaves.limit.CreateLimits
import cn.xor7.xiaohei.feaves.limit.Limits
import cn.xor7.xiaohei.feaves.runInBotDataManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.util.NoSuchElementException
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals


class BotDataManagerTest {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = false
    }
    private val testDataFileContent = json.encodeToString(mutableMapOf(
        NOTHING to BotData(),
        BOT_WITH_NOTHING to BotData(storage = StorageType.BOT),
        PLAYER_WITH_CREATE_LOCATION_LIMIT to BotData(
            storage = StorageType.PLAYER,
            limits = Limits(
                create = CreateLimits(
                    locations = mutableSetOf(
                        LocationData(x = 0.0, y = 0.0, z = 0.0, world = "overworld"),
                        LocationData(x = 10.0, y = 10.0, z = 10.0, world = "nether")
                    )
                )
            )
        ),
        PLAYER_WITH_ACTION_LIMIT to BotData(
            storage = StorageType.PLAYER,
            limits = Limits(
                actions = mutableMapOf(
                    "*" to ActionLimit(enable = false),
                    "use" to ActionLimit(enable = true)
                )
            )
        )
    ))

    @BeforeEach
    fun init() {
        initMock()
        val file = File(DATA_FILE)
        file.parentFile.mkdirs()
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.writeText(testDataFileContent)
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
            LocationData(
                x = 0.0, y = 0.0, z = 0.0, world = "overworld"
            )
        )

        assertContains(
            locations,
            LocationData(
                x = 10.0, y = 10.0, z = 10.0, world = "nether"
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
    }
}