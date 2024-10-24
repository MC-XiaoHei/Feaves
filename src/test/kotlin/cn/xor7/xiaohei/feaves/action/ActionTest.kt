package cn.xor7.xiaohei.feaves.action

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ActionTest {
    @Test
    fun testDefaultNamespace() {
        assertEquals(ActionNamespace.FEAVES, defaultActionNamespace)
    }

    @Test
    fun testFromKey() {
        assertEquals(
            Action(ActionNamespace.FEAVES, "use"),
            Action.fromKey("feaves.use")
        )
    }

    @Test
    fun testFromKeyThrows() {
        assertThrows<IllegalArgumentException> {
            Action.fromKey("feaves")
        }
        assertThrows<IllegalArgumentException> {
            Action.fromKey("unknown-namespace.use")
        }
    }


    @Test
    fun testFromDisplayName() {
        assertEquals(
            Action(ActionNamespace.FEAVES, "use"),
            Action.fromDisplayName("use")
        )
        assertEquals(
            Action(ActionNamespace.LEAVES, "use"),
            Action.fromDisplayName("leaves.use")
        )
    }
}