package y21

import common.puzzle.Input
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day21Test {
    private lateinit var day: Day21

    @BeforeEach
    fun setUp() {
        day = Day21(Input("""
            Player 1 starting position: 4
            Player 2 starting position: 8
        """.trimIndent()))
    }

    @Test
    fun solveLevel1() {
        assertEquals(739785L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(444356092776315L, day.solveLevel2())
    }
}
