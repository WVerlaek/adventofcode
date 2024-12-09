package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {
    private val sample = Input("""
    2333133121414131402
    """.trimIndent())

    private val day = Day9(sample)

    @Test
    fun solveLevel1() {
        assertEquals(1928L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(2858L, day.solveLevel2())
    }
}
