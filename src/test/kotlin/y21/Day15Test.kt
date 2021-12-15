package y21

import common.puzzle.Input
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day15Test {
    lateinit var day: Day15

    @BeforeEach
    fun setUp() {
        day = Day15(Input("""
            1163751742
            1381373672
            2136511328
            3694931569
            7463417111
            1319128137
            1359912421
            3125421639
            1293138521
            2311944581
        """.trimIndent()))
    }

    @Test
    fun solveLevel1() {
        assertEquals(40, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(315, day.solveLevel2())
    }
}
