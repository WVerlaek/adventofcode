package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {
    private val sample = Input("3,4,3,1,2")

    private val day = Day6(sample)

    @Test
    fun solveLevel1() {
        assertEquals(5934, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(26984457539L, day.solveLevel2())
    }
}
