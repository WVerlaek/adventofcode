package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {
    private val sample = Input("""
    >>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
    """.trimIndent())

    private val day = Day17(sample)

    @Test
    fun solveLevel1() {
        assertEquals(3068, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(1514285714288L, day.solveLevel2())
    }
}
