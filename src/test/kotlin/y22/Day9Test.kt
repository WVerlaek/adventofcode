package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {
    private val sample = Input("""
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
    """.trimIndent())

    private val day = Day9(sample)

    @Test
    fun solveLevel1() {
        assertEquals(13, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(1, day.solveLevel2())

        assertEquals(36, Day9(Input("""
        R 5
        U 8
        L 8
        D 3
        R 17
        D 10
        L 25
        U 20
        """.trimIndent())).solveLevel2())
    }
}
