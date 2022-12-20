package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {
    private val sample = Input("""
    1
    2
    -3
    3
    -2
    0
    4
    """.trimIndent())

    private val day = Day20(sample)

    @Test
    fun solveLevel1() {
        assertEquals(3L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(1623178306L, day.solveLevel2())
    }
}
