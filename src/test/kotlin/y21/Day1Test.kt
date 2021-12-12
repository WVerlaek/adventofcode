package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {
    private val sample = Input("""
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent())

    private val day = Day1(sample)

    @Test
    fun solveLevel1() {
        assertEquals(7, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(5, day.solveLevel2())
    }
}
