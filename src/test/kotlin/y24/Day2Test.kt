package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {
    private val sample = Input("""
    7 6 4 2 1
    1 2 7 8 9
    9 7 6 2 1
    1 3 2 4 5
    8 6 4 4 1
    1 3 6 7 9
    """.trimIndent())

    private val day = Day2(sample)

    @Test
    fun solveLevel1() {
        assertEquals(2, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(4, day.solveLevel2())

        assertEquals(1, Day2(Input("29 32 30 31 34 35 37")).solveLevel2())
        assertEquals(1, Day2(Input("27 25 26 27 30 31")).solveLevel2())
    }
}
