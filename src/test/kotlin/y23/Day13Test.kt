package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day13Test {
    private val sample = Input("""
    #.##..##.
    ..#.##.#.
    ##......#
    ##......#
    ..#.##.#.
    ..##..##.
    #.#.##.#.
    
    #...##..#
    #....#..#
    ..##..###
    #####.##.
    #####.##.
    ..##..###
    #....#..#
    """.trimIndent())

    private val day = Day13(sample)

    @Test
    fun solveLevel1() {
        assertEquals(405, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(400, day.solveLevel2())
    }
}
