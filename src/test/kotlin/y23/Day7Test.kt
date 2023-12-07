package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7Test {
    private val sample = Input("""
    32T3K 765
    T55J5 684
    KK677 28
    KTJJT 220
    QQQJA 483
    """.trimIndent())

    private val day = Day7(sample)

    @Test
    fun solveLevel1() {
        assertEquals(6440, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(5905, day.solveLevel2())
    }
}
