package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day23Test {
    private val sample = Input("""
    ....#..
    ..###.#
    #...#.#
    .#...##
    #.###..
    ##.#.##
    .#..#..
    """.trimIndent())

    private val day = Day23(sample)

    @Test
    fun solveLevel1() {
        assertEquals(110, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(20, day.solveLevel2())
    }
}
