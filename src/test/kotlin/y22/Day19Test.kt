package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day19Test {
    private val sample = Input("""
    Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
    Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent())

    private val day = Day19(sample)

    @Test
    fun solveLevel1() {
        assertEquals(33, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // Commented out, takes a while to run.
        // assertEquals(3472, day.solveLevel2())
    }
}
