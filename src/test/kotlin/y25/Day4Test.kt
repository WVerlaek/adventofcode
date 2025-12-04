package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day4Test {
    private val sample = Input("""
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
    """.trimIndent())

    private val day = Day4(sample)

    @Test
    fun solveLevel1() {
        assertEquals(13, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(43, day.solveLevel2())
    }
}
