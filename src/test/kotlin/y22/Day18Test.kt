package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day18Test {
    private val sample = Input("""
    2,2,2
    1,2,2
    3,2,2
    2,1,2
    2,3,2
    2,2,1
    2,2,3
    2,2,4
    2,2,6
    1,2,5
    3,2,5
    2,1,5
    2,3,5
    """.trimIndent())

    private val day = Day18(sample)

    @Test
    fun solveLevel1() {
        assertEquals(64, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(58, day.solveLevel2())
    }
}
