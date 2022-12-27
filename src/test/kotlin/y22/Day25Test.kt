package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day25Test {
    private val sample = Input("""
    1=-0-2
    12111
    2=0=
    21
    2=01
    111
    20012
    112
    1=-1=
    1-12
    12
    1=
    122
    """.trimIndent())

    private val day = Day25(sample)

    @Test
    fun solveLevel1() {
        assertEquals("2=-1=0", day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // assertEquals(, day.solveLevel2())
    }
}
