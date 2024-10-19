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
import kotlin.uuid.Uuid


class BotDataManagerTest {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = false
    }
    private val testDataFileContent = json.encodeToString(
        mutableMapOf(
            NOTHING_UUID to BotData(NOTHING_NAME),
            BOT_WITH_NOTHING_UUID to BotData(
                name = BOT_WITH_NOTHING_NAME,
                storage = StorageType.BOT
            ),
            PLAYER_WITH_CREATE_LOCATION_LIMIT_UUID to BotData(
                name = PLAYER_WITH_CREATE_LOCATION_LIMIT_NAME,
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
            PLAYER_WITH_ACTION_LIMIT_UUID to BotData(
                name = PLAYER_WITH_ACTION_LIMIT_NAME,
                storage = StorageType.PLAYER,
                limits = Limits(
                    actions = mutableMapOf(
                        "@" to ActionLimit(enable = false),
                        "use" to ActionLimit(enable = true)
                    )
                )
            )
        )
    )

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
    fun testUuidToNameConvert() = runInBotDataManager {
        assertEquals(
            getBotData(NOTHING_NAME),
            getBotData(NOTHING_UUID.toUuid())
        )
        assertEquals(
            getBotData(BOT_WITH_NOTHING_NAME),
            getBotData(BOT_WITH_NOTHING_UUID.toUuid())
        )
        assertEquals(
            getBotData(PLAYER_WITH_CREATE_LOCATION_LIMIT_NAME),
            getBotData(PLAYER_WITH_CREATE_LOCATION_LIMIT_UUID.toUuid())
        )
        assertEquals(
            getBotData(PLAYER_WITH_ACTION_LIMIT_NAME),
            getBotData(PLAYER_WITH_ACTION_LIMIT_UUID.toUuid())
        )
    }

    @Test
    fun testLoadBotStorageType() = runInBotDataManager {
        val botData1 = copyBotData(BOT_WITH_NOTHING_UUID.toUuid())
        assertEquals(StorageType.BOT, botData1.storage)

        val botData2 = copyBotData(PLAYER_WITH_CREATE_LOCATION_LIMIT_UUID.toUuid())
        assertEquals(StorageType.PLAYER, botData2.storage)

        val botData3 = copyBotData(NOTHING_UUID.toUuid())
        assertEquals(StorageType.BOT, botData3.storage)
    }

    @Test
    fun testLoadBotActionLimit() = runInBotDataManager {
        val actionLimit = copyBotData(PLAYER_WITH_ACTION_LIMIT_UUID.toUuid()).limits.actions
        assertEquals(2, actionLimit.size)

        assertEquals(
            ActionLimit(
                enable = false
            ),
            actionLimit["@"]
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
        val locations = copyBotData(PLAYER_WITH_CREATE_LOCATION_LIMIT_UUID.toUuid()).limits.create.locations
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

    private fun String.toUuid() = Uuid.parse(this)

    companion object {
        const val NOTHING_UUID = "2a0a79c4-3a69-47fc-8f92-06956b16af4e"
        const val NOTHING_NAME = "Nothing"
        const val BOT_WITH_NOTHING_UUID = "01f255ca-73cd-4192-a655-ee067a62f1df"
        const val BOT_WITH_NOTHING_NAME = "BotWithNothing"
        const val PLAYER_WITH_CREATE_LOCATION_LIMIT_UUID = "d32f8284-e5d7-4829-aa6a-9dddc54db163"
        const val PLAYER_WITH_CREATE_LOCATION_LIMIT_NAME = "PlayerWithCreateLocationLimit"
        const val PLAYER_WITH_ACTION_LIMIT_UUID = "1eba35f5-c778-4e6a-9925-97041bde6df7"
        const val PLAYER_WITH_ACTION_LIMIT_NAME = "PlayerWithActionLimit"
    }
}