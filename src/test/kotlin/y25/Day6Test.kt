package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {
    private val sample = Input("""
123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  
    """.trimIndent())

    private val day = Day6(sample)

    @Test
    fun solveLevel1() {
        assertEquals(4277556L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(3263827L, day.solveLevel2())
    }
}
