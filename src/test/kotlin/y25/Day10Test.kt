package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {
    private val sample = Input("""
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent())

    private val day = Day10(sample)

    @Test
    fun solveLevel1() {
        assertEquals(7, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // Doesn't work in CI (z3 solver)
        // assertEquals(33, day.solveLevel2())
    }
}
