package cn.xor7.xiaohei.feaves.action

enum class ActionNamespace(val value: String) {
    LEAVES("leaves"),
    FEAVES("feaves"),
    ;

    override fun toString() = value
}

val defaultActionNamespace = try {
    Class.forName("org.leavesmc.leaves.LeavesConfig")
    ActionNamespace.LEAVES
} catch (_: ClassNotFoundException) {
    ActionNamespace.FEAVES
}