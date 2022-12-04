package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day4Test {
    private val sample = Input("""
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
    """.trimIndent())

    private val day = Day4(sample)

    @Test
    fun solveLevel1() {
        assertEquals(2, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(4, day.solveLevel2())
    }
}
