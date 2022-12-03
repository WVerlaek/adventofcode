package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {
    private val sample = Input("""
    vJrwpWtwJgWrhcsFMMfFFhFp
    jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
    PmmdzqPrVvPwwTWBwg
    wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
    ttgJtRGJQctTZtZT
    CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent())

    private val day = Day3(sample)

    @Test
    fun solveLevel1() {
        assertEquals(157, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(70, day.solveLevel2())
    }
}
