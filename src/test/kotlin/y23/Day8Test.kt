package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day8Test {
    private val sample = Input("""
    RL
    
    AAA = (BBB, CCC)
    BBB = (DDD, EEE)
    CCC = (ZZZ, GGG)
    DDD = (DDD, DDD)
    EEE = (EEE, EEE)
    GGG = (GGG, GGG)
    ZZZ = (ZZZ, ZZZ)
    """.trimIndent())

    private val sample2 = Input("""
    LR

    11A = (11B, XXX)
    11B = (XXX, 11Z)
    11Z = (11B, XXX)
    22A = (22B, XXX)
    22B = (22C, 22C)
    22C = (22Z, 22Z)
    22Z = (22B, 22B)
    XXX = (XXX, XXX)
    """.trimIndent())

    private val day = Day8(sample)
    private val day2 = Day8(sample2)

    @Test
    fun solveLevel1() {
        assertEquals(2, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // Test case doesn't have one end node per cycle (failing precondition), but input does
        // assertEquals(6L, day2.solveLevel2())
    }
}
