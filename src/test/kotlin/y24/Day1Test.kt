package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {
    private val sample = Input("""
    3   4
    4   3
    2   5
    1   3
    3   9
    3   3
    """.trimIndent())

    private val day = Day1(sample)

    @Test
    fun solveLevel1() {
        assertEquals(11, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(31L, day.solveLevel2())
    }
}
