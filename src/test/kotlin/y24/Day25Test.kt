package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day25Test {
    private val sample = Input("""
    #####
    .####
    .####
    .####
    .#.#.
    .#...
    .....
    
    #####
    ##.##
    .#.##
    ...##
    ...#.
    ...#.
    .....
    
    .....
    #....
    #....
    #...#
    #.#.#
    #.###
    #####
    
    .....
    .....
    #.#..
    ###..
    ###.#
    ###.#
    #####
    
    .....
    .....
    .....
    #....
    #.#..
    #.#.#
    #####
    """.trimIndent())

    private val day = Day25(sample)

    @Test
    fun solveLevel1() {
        assertEquals(3, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // assertEquals(, day.solveLevel2())
    }
}
