package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {
    private val sample = Input("""
    125 17
    """.trimIndent())

    private val day = Day11(sample)

    @Test
    fun solveLevel1() {
        assertEquals(55312L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // No sample
    }
}
