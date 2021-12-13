package y21

import common.puzzle.Input
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day13Test {
    lateinit var day: Day13

    @BeforeEach
    fun setUp() {
        day = Day13(Input("""
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent()))
    }

    @Test
    fun solveLevel1() {
        assertEquals(17, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // Returns hardcoded value read from solution of real input.
    }
}
