package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {
    private val sample = Input("""
    ...#......
    .......#..
    #.........
    ..........
    ......#...
    .#........
    .........#
    ..........
    .......#..
    #...#.....
    """.trimIndent())

    private val day = Day11(sample)

    @Test
    fun solveLevel1() {
        assertEquals(374L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(8410L, Day11(sample, deltaOverride = 99).solveLevel2())
    }
}
