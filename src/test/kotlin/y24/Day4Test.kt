package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day4Test {
    private val sample = Input("""
    MMMSXXMASM
    MSAMXMSMSA
    AMXSXMAAMM
    MSAMASMSMX
    XMASAMXAMM
    XXAMMXXAMA
    SMSMSASXSS
    SAXAMASAAA
    MAMMMXMMMM
    MXMXAXMASX
    """.trimIndent())

    private val day = Day4(sample)

    @Test
    fun solveLevel1() {
        assertEquals(18, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(9, day.solveLevel2())
    }
}
