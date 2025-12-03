package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {
    private val sample = Input("""
987654321111111
811111111111119
234234234234278
818181911112111
    """.trimIndent())

    private val day = Day3(sample)

    @Test
    fun solveLevel1() {
        assertEquals(357L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(3121910778619L, day.solveLevel2())
    }
}
