package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {
    private val sample = Input("""
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent())

    private val day = Day12(sample)

    @Test
    fun solveLevel1() {
        assertEquals(10, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(36, day.solveLevel2())
    }
}
