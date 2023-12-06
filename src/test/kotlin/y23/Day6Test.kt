package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {
    private val sample = Input("""
    Time:      7  15   30
    Distance:  9  40  200
    """.trimIndent())

    private val day = Day6(sample)

    @Test
    fun solveLevel1() {
        assertEquals(288L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(71503L, day.solveLevel2())
    }
}
