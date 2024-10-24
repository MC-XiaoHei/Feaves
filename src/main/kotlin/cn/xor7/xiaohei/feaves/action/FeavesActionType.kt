package cn.xor7.xiaohei.feaves.action

enum class FeavesActionType {
    USE,
    ATTACK,
    MOUNT,
    DISMOUNT,
    DROP,
    JUMP,
    LOOK,
    MOVE,
    SNEAK,
    UNSNEAK,
    SPRINT,
    UNSPRINT,
    TURN,
    SWAP_HANDS
    ;

    override fun toString() = name.lowercase().replace('_', '-')
}