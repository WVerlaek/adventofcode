package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7Test {
    private val sample = Input("""
    190: 10 19
    3267: 81 40 27
    83: 17 5
    156: 15 6
    7290: 6 8 6 15
    161011: 16 10 13
    192: 17 8 14
    21037: 9 7 18 13
    292: 11 6 16 20
    """.trimIndent())

    private val day = Day7(sample)

    @Test
    fun solveLevel1() {
        assertEquals(3749L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(11387L, day.solveLevel2())
    }
}
