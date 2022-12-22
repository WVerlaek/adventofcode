package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {
    private val sample = Input("""
            ...#
            .#..
            #...
            ....
    ...#.......#
    ........#...
    ..#....#....
    ..........#.
            ...#....
            .....#..
            .#......
            ......#.

    10R5L5R10L4R5L5
    """.trimIndent())

    private val day = Day22(sample)

    @Test
    fun solveLevel1() {
        assertEquals(6032, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // Not making wrapping rules for the test ¯\_(ツ)_/¯
    }
}
