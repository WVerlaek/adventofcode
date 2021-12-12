package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {
    private val sample = Input("""
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent())

    private val day = Day2(sample)

    @Test
    fun solveLevel1() {
        assertEquals(150L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(900L, day.solveLevel2())
    }
}
