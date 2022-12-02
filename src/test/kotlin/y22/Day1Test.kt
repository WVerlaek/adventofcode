package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {
    private val sample = Input("""
        1000
        2000
        3000

        4000

        5000
        6000

        7000
        8000
        9000

        10000
    """.trimIndent())

    private val day = Day1(sample)

    @Test
    fun solveLevel1() {
        assertEquals(24000, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(45000, day.solveLevel2())
    }
}
