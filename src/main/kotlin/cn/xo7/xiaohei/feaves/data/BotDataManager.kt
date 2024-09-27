package cn.xo7.xiaohei.feaves.data

import cn.xo7.xiaohei.feaves.Feaves
import cn.xo7.xiaohei.feaves.PROJECT_NAME
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

const val DATA_FILE = "./plugins/$PROJECT_NAME/data.json"

object BotDataManager {
    private lateinit var data: BotDataFileModule
    private val dataFile: File = File(DATA_FILE)
    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    suspend fun run(block: suspend BotDataManager.() -> Unit) {
        withContext(Feaves.globalRegionDispatcher) {
            BotDataManager.block()
        }
    }

    fun BotDataManager.getBotData(uuid: UUIDString): BotData {
        return data[uuid] ?: throw IllegalArgumentException("Bot with UUID $uuid not found.")
    }

    init {
        if (dataFile.isFile) {
            load()
        } else {
            dataFile.createNewFile()
            data = mutableMapOf()
        }
        save()
    }

    fun load() {
        data = json.decodeFromString<BotDataFileModule>(dataFile.readText())
    }

    fun save() = dataFile.writeText(json.encodeToString<BotDataFileModule>(data))
}