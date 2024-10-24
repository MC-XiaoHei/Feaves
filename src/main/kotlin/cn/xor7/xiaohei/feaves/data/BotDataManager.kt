package cn.xor7.xiaohei.feaves.data

import cn.xor7.xiaohei.feaves.PROJECT_NAME
import cn.xor7.xiaohei.feaves.data.BotDataManager.getBotData
import cn.xor7.xiaohei.feaves.feavesInstance
import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.leavesmc.leaves.entity.Bot
import java.io.File
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

object BotDataManager {
    const val DATA_FILE = "./plugins/${PROJECT_NAME}/data.json"
    private lateinit var data: BotDataFileModule
    private val uuidByName = mutableMapOf<String, Uuid>()
    private val dataFile: File = File(DATA_FILE)
    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        encodeDefaults = false
        serializersModule = SerializersModule {
            contextual(Uuid.serializer())
        }
    }

    suspend fun <R> run(block: suspend BotDataManager.() -> R): R {
        return withContext(feavesInstance.globalRegionDispatcher) {
            BotDataManager.block()
        }
    }

    suspend fun BotDataManager.getBotData(uuid: Uuid): BotData = run {
        return@run data[uuid] ?: throw NoSuchElementException("Bot with UUID $uuid not found.")
    }

    suspend fun BotDataManager.getBotData(name: String): BotData = run {
        return@run getBotData(name.botUuid)
    }

    suspend fun copyBotData(uuid: Uuid): BotData = run {
        return@run getBotData(uuid).copy()
    }

    suspend fun copyBotData(name: String): BotData = run {
        return@run copyBotData(name.botUuid)
    }

    suspend fun botNames(): List<String> = run {
        return@run data.values.map { it.name }
    }

    suspend fun botUuids(): List<Uuid> = run {
        return@run data.keys.toList()
    }

    val String.botUuid: Uuid
        get() = uuidByName[this] ?: throw NoSuchElementException("Bot with name $this not found.")

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
        generateUuidByName()
    }

    fun generateUuidByName() {
        data.forEach { (uuid, botData) ->
            uuidByName[botData.name] = uuid
        }
    }

    fun save() = dataFile.writeText(json.encodeToString<BotDataFileModule>(data))
}

suspend fun Bot.data(): BotData = BotDataManager.getBotData(uniqueId.toKotlinUuid())