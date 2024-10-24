package cn.xor7.xiaohei.feaves.action

import org.leavesmc.leaves.entity.botaction.BotActionType

data class Action(
    val namespace: ActionNamespace = defaultActionNamespace,
    val name: String,
) {
    val key = "$namespace.$name"
    val displayName: String = if (namespace == defaultActionNamespace) name else key

    companion object {
        fun fromKey(key: String): Action {
            val parts = key.split(".")
            if (parts.size != 2) throw IllegalArgumentException("Invalid action key: $key")
            return Action(ActionNamespace.valueOf(parts[0].uppercase()), parts[1])
        }

        fun fromDisplayName(displayName: String): Action = run {
            if ("." in displayName) fromKey(displayName)
            else Action(name = displayName)
        }
    }
}

val actions = mutableSetOf<Action>().apply {
    BotActionType.entries.forEach { add(Action(ActionNamespace.LEAVES, it.name.lowercase())) }
    FeavesActionType.entries.forEach { add(Action(ActionNamespace.FEAVES, it.toString())) }
}