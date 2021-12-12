package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {
    private val sample = Input("""
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010
    """.trimIndent())

    private val day = Day3(sample)

    @Test
    fun solveLevel1() {
        assertEquals(198L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(230L, day.solveLevel2())
    }
}
