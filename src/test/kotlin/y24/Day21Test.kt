package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {
    private val sample = Input("""
    029A
    980A
    179A
    456A
    379A
    """.trimIndent())

    private val day = Day21(sample)

    @Test
    fun solveLevel1() {
        assertEquals(126384L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(154115708116294L, day.solveLevel2())
    }
}
