package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {
    private val sample = Input("""
    A Y
    B X
    C Z
    """.trimIndent())

    private val day = Day2(sample)

    @Test
    fun solveLevel1() {
        assertEquals(15, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(12, day.solveLevel2())
    }
}
