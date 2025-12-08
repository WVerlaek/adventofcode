package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day8Test {
    private val sample = Input("""
162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689
    """.trimIndent())

    private val day = Day8(sample)

    @Test
    fun solveLevel1() {
        assertEquals(40, day.solveLevel1(n = 10))
    }

    @Test
    fun solveLevel2() {
         assertEquals(25272L, day.solveLevel2())
    }
}
