package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test {
    private val sample = Input("""
        [D]
    [N] [C]
    [Z] [M] [P]
     1   2   3

    move 1 from 2 to 1
    move 3 from 1 to 3
    move 2 from 2 to 1
    move 1 from 1 to 2
    """.trimIndent())

    private val day = Day5(sample)

    @Test
    fun solveLevel1() {
        assertEquals("CMZ", day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals("MCD", day.solveLevel2())
    }
}
