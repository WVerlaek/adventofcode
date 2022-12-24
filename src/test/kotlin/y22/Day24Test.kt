package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day24Test {
    private val sample = Input("""
    #.######
    #>>.<^<#
    #.<..<<#
    #>v.><>#
    #<^v^^>#
    ######.#
    """.trimIndent())

    private val day = Day24(sample)

    @Test
    fun solveLevel1() {
        assertEquals(18, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(54, day.solveLevel2())
    }
}
