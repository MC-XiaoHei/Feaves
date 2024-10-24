package cn.xor7.xiaohei.feaves.config

import cn.xor7.xiaohei.feaves.feavesInstance
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader

object FeavesConfig {
    private val loader = HoconConfigurationLoader.builder()
        .file(feavesInstance.dataFolder.resolve("config.conf"))
        .emitComments(true)
        .prettyPrinting(true)
        .build()
    private lateinit var node: CommentedConfigurationNode
    private lateinit var module: FeavesConfigModule

    init {
        load()
    }

    fun load() {
        node = loader.load()
        module = node.get(FeavesConfigModule::class.java) ?: FeavesConfigModule()
        save()
    }

    fun save() = loader.save(node.set(module::class.java, module))

    fun module(): FeavesConfigModule = module
}