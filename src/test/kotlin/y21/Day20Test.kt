package y21

import common.puzzle.Input
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day20Test {
    private lateinit var day: Day20

    @BeforeEach
    fun setUp() {
        day = Day20(Input("""
            ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

            #..#.
            #....
            ##..#
            ..#..
            ..###
        """.trimIndent()))
    }

    @Test
    fun solveLevel1() {
        assertEquals(35, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(3351, day.solveLevel2())
    }
}
