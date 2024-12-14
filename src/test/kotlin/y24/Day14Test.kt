package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {
    private val sample = Input("""
    p=0,4 v=3,-3
    p=6,3 v=-1,-3
    p=10,3 v=-1,2
    p=2,0 v=2,-1
    p=0,0 v=1,3
    p=3,0 v=-2,-2
    p=7,6 v=-1,-3
    p=3,0 v=-1,-2
    p=9,3 v=2,3
    p=7,3 v=-1,2
    p=2,4 v=2,-3
    p=9,5 v=-3,-3
    """.trimIndent())

    private val day = Day14(sample, rows = 7, cols = 11)

    @Test
    fun solveLevel1() {
        assertEquals(12, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // Manual solution
    }
}
