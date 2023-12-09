package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {
    private val sample = Input("""
    0 3 6 9 12 15
    1 3 6 10 15 21
    10 13 16 21 30 45
    """.trimIndent())

    private val day = Day9(sample)

    @Test
    fun solveLevel1() {
        assertEquals(114, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        val day = Day9(Input("10 13 16 21 30 45"))
        assertEquals(5, day.solveLevel2())
    }
}
