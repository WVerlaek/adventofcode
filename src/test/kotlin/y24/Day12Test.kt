package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {
    private val sample = Input("""
    AAAA
    BBCD
    BBCC
    EEEC
    """.trimIndent())

    private val day = Day12(sample)

    @Test
    fun solveLevel1() {
        assertEquals(140, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(80, day.solveLevel2())
    }
}
