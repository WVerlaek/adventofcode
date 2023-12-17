package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {
    private val sample = Input("""
    2413432311323
    3215453535623
    3255245654254
    3446585845452
    4546657867536
    1438598798454
    4457876987766
    3637877979653
    4654967986887
    4564679986453
    1224686865563
    2546548887735
    4322674655533
    """.trimIndent())

    private val day = Day17(sample)

    @Test
    fun solveLevel1() {
        assertEquals(102, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(94, day.solveLevel2())
    }

    @Test
    fun solveLevel2_2() {
        assertEquals(71, Day17(Input("""
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991
        """.trimIndent())).solveLevel2())
    }
}
