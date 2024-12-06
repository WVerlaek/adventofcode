package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {
    private val sample = Input("""
    ....#.....
    .........#
    ..........
    ..#.......
    .......#..
    ..........
    .#..^.....
    ........#.
    #.........
    ......#...
    """.trimIndent())

    private val day = Day6(sample)

    @Test
    fun solveLevel1() {
        assertEquals(41, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(6, day.solveLevel2())
    }
}
