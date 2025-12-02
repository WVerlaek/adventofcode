package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {
    private val sample = Input("""
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
    """.trimIndent())

    private val day = Day1(sample)

    @Test
    fun solveLevel1() {
        assertEquals(3, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(6, day.solveLevel2())
    }
}
