package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day8Test {
    private val sample = Input("""
    30373
    25512
    65332
    33549
    35390
    """.trimIndent())

    private val day = Day8(sample)

    @Test
    fun solveLevel1() {
        assertEquals(21, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(8, day.solveLevel2())
    }
}
