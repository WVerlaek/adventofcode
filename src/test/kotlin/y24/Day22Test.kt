package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {
    private val sample = Input("""
    1
    10
    100
    2024
    """.trimIndent())

    private val day = Day22(sample)

    @Test
    fun solveLevel1() {
        assertEquals(37327623L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(23L, Day22(Input("""
         1
         2
         3
         2024
         """.trimIndent())).solveLevel2())
    }
}
