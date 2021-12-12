package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {
    private val sample = Input("""
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent())

    private val day = Day9(sample)

    @Test
    fun solveLevel1() {
        assertEquals(15, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(1134L, day.solveLevel2())
    }
}
