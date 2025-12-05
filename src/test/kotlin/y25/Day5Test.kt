package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test {
    private val sample = Input("""
3-5
10-14
16-20
12-18

1
5
8
11
17
32
    """.trimIndent())

    private val day = Day5(sample)

    @Test
    fun solveLevel1() {
        assertEquals(3, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(14L, day.solveLevel2())
    }
}
