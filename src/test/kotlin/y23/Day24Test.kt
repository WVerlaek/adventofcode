package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day24Test {
    private val sample = Input("""
    19, 13, 30 @ -2,  1, -2
    18, 19, 22 @ -1, -1, -2
    20, 25, 34 @ -2, -2, -4
    12, 31, 28 @ -1, -2, -1
    20, 19, 15 @  1, -5, -3
    """.trimIndent())

    private val day = Day24(sample)

    @Test
    fun solveLevel1() {
        assertEquals(2, day.numIntersections(7, 27))
    }

    @Test
    fun solveLevel2() {
        // Z3 link error in CI
        // assertEquals(47L, day.solveLevel2())
    }
}
