package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day19Test {
    private val sample = Input("""
    r, wr, b, g, bwu, rb, gb, br
    
    brwrr
    bggr
    gbbr
    rrbgbr
    ubwu
    bwurrg
    brgr
    bbrgwb
    """.trimIndent())

    private val day = Day19(sample)

    @Test
    fun solveLevel1() {
        assertEquals(6, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(16L, day.solveLevel2())
    }
}
