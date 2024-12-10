package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {
    private val sample = Input("""
    89010123
    78121874
    87430965
    96549874
    45678903
    32019012
    01329801
    10456732
    """.trimIndent())

    private val day = Day10(sample)

    @Test
    fun solveLevel1() {
        assertEquals(36, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(81, day.solveLevel2())
    }
}
