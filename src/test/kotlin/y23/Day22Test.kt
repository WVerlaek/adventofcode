package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {
    private val sample = Input("""
    1,0,1~1,2,1
    0,0,2~2,0,2
    0,2,3~2,2,3
    0,0,4~0,2,4
    2,0,5~2,2,5
    0,1,6~2,1,6
    1,1,8~1,1,9
    """.trimIndent())

    private val day = Day22(sample)

    @Test
    fun solveLevel1() {
        assertEquals(5, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(7, day.solveLevel2())
    }
}
