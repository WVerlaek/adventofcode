package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {
    private val sample = Input("""
7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3
    """.trimIndent())

    private val day = Day9(sample)

    @Test
    fun solveLevel1() {
        assertEquals(50L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(24L, day.solveLevel2())
    }
}
