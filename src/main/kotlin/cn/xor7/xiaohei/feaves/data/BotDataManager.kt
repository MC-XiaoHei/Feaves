package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.INSTANCE
import cn.xor7.xiaohei.feaves.PROJECT_NAME
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object BotDataManager {
    const val DATA_FILE = "./plugins/${PROJECT_NAME}/data.json"
    private lateinit var data: BotDataFileModule
    private val dataFile: File = File(DATA_FILE)
    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    suspend fun <R> run(block: suspend BotDataManager.() -> R): R {
        return withContext(INSTANCE.globalRegionDispatcher) {
            BotDataManager.block()
        }
    }

    suspend fun BotDataManager.getBotData(uuid: UUIDString): BotData = run {
        return@run data[uuid] ?: throw NoSuchElementException("Bot with UUID $uuid not found.")
    }

    suspend fun copyBotData(uuid: UUIDString): BotData = run {
        return@run getBotData(uuid).copy()
    }

    init {
        init()
    }

    fun init() {
        if (dataFile.isFile) {
            load()
        } else {
            data = mutableMapOf()
            dataFile.createNewFile()
        }
        save()
    }

    fun load() {
        data = json.decodeFromString<BotDataFileModule>(dataFile.readText())
    }

    fun save() = dataFile.writeText(json.encodeToString<BotDataFileModule>(data))
}