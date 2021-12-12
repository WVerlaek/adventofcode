package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test {
    private val sample = Input("""
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent())

    private val day = Day5(sample)

    @Test
    fun solveLevel1() {
        assertEquals(5, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(12, day.solveLevel2())
    }
}
