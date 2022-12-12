package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {
    private val sample = Input("""
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
    """.trimIndent())

    private val day = Day12(sample)

    @Test
    fun solveLevel1() {
        assertEquals(31, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(29, day.solveLevel2())
    }
}
