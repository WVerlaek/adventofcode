package y21

import common.puzzle.Input
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day23Test {
    private lateinit var day: Day23

    @BeforeEach
    fun setUp() {
        day = Day23(Input("""
            #############
            #...........#
            ###B#C#B#D###
              #A#D#C#A#
              #########
        """.trimIndent()))
    }

    @Test
    fun solveLevel1() {
//        assertEquals(12521, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(44169, day.solveLevel2())
    }
}
