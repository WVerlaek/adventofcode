package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {
    private val sample = Input("""
    Register A: 729
    Register B: 0
    Register C: 0
    
    Program: 0,1,5,4,3,0
    """.trimIndent())

    private val day = Day17(sample)

    @Test
    fun solveLevel1() {
        assertEquals("4,6,3,5,6,3,5,2,1,0", day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(117440L, Day17(Input("""
             Register A: 2024
             Register B: 0
             Register C: 0

             Program: 0,3,5,4,3,0
         """.trimIndent())).solveLevel2())
    }
}
