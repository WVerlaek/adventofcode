package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test {
    private val sample = Input("""
    47|53
    97|13
    97|61
    97|47
    75|29
    61|13
    75|53
    29|13
    97|29
    53|29
    61|53
    97|53
    61|29
    47|13
    75|47
    97|75
    47|61
    75|61
    47|29
    75|13
    53|13
    
    75,47,61,53,29
    97,61,53,29,13
    75,29,13
    75,97,47,61,53
    61,13,29
    97,13,75,29,47
    """.trimIndent())

    private val day = Day5(sample)

    @Test
    fun solveLevel1() {
        assertEquals(143, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(123, day.solveLevel2())
    }
}
