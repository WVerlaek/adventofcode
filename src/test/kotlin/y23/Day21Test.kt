package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {
    private val sample = Input("""
    ...........
    .....###.#.
    .###.##..#.
    ..#.#...#..
    ....#.#....
    .##..S####.
    .##..#...#.
    .......##..
    .##.#.####.
    .##..##.##.
    ...........
    """.trimIndent())

    private val day = Day21(sample)

    @Test
    fun solveLevel1() {
        assertEquals(16, day.reachablePlots(6))
    }

    @Test
    fun solveLevel2() {
        // Level 2 solution doesn't work on sample input.
    }
}
