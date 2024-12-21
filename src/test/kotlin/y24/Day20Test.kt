package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {
    private val sample = Input("""
    ###############
    #...#...#.....#
    #.#.#.#.#.###.#
    #S#...#.#.#...#
    #######.#.#.###
    #######.#.#...#
    #######.#.###.#
    ###..E#...#...#
    ###.#######.###
    #...###...#...#
    #.#####.#.###.#
    #.#...#.#.#...#
    #.#.#.#.#.#.###
    #...#...#...###
    ###############
    """.trimIndent())

    private val day = Day20(sample, 12)

    @Test
    fun solveLevel1() {
        assertEquals(8, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(29, Day20(sample, 72).solveLevel2())
    }
}
