package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7Test {
    private val sample = Input("16,1,2,0,4,2,7,1,2,14")

    private val day = Day7(sample)

    @Test
    fun solveLevel1() {
        assertEquals(37, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(168, day.solveLevel2())
    }
}
