package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day18Test {
    private val sample = Input("""
    5,4
    4,2
    4,5
    3,0
    2,1
    6,3
    2,4
    1,5
    0,6
    3,3
    2,6
    5,1
    1,2
    5,5
    2,5
    6,5
    1,4
    0,4
    6,4
    1,1
    6,1
    1,0
    0,5
    1,6
    2,0
    """.trimIndent())

    private val day = Day18(sample, size = 7, takeBytes = 12)

    @Test
    fun solveLevel1() {
        assertEquals(22, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals("6,1", day.solveLevel2())
    }
}
