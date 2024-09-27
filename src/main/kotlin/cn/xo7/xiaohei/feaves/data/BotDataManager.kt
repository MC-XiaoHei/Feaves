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
    private lateinit var data: FileModule
    private val dataFile: File
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
        return data.bots[uuid] ?: throw IllegalArgumentException("Bot with UUID $uuid not found.")
    }

    init {
        var file = File(DATA_FILE)
        if (!file.isFile) {
            file.createNewFile()
            file.writeText("{}")
        }
        dataFile = file
        load()
    }

    fun load() {
        data = json.decodeFromString<FileModule>(dataFile.readText())
        save()
    }

    fun save() = dataFile.writeText(json.encodeToString<FileModule>(data))
}