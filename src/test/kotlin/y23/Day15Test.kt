package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test {
    private val sample = Input("""
    rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    """.trimIndent())

    private val day = Day15(sample)

    @Test
    fun solveLevel1() {
        assertEquals(1320, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(145, day.solveLevel2())
    }
}
