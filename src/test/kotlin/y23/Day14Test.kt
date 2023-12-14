package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {
    private val sample = Input("""
    O....#....
    O.OO#....#
    .....##...
    OO.#O....O
    .O.....O#.
    O.#..O.#.#
    ..O..#O..O
    .......O..
    #....###..
    #OO..#....
    """.trimIndent())

    private val day = Day14(sample)

    @Test
    fun solveLevel1() {
        assertEquals(136, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(64, day.solveLevel2())
    }
}
