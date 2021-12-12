package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {
    private val sample = Input("""
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent())

    private val day = Day11(sample)

    @Test
    fun solveLevel1() {
        assertEquals(1656L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(195, day.solveLevel2())
    }
}
