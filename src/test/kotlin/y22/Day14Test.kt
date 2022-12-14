package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {
    private val sample = Input("""
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent())

    private val day = Day14(sample)

    @Test
    fun solveLevel1() {
        assertEquals(24, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(93, day.solveLevel2())
    }
}
