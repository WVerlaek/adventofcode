package y21

import common.puzzle.Input
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day17Test {
    lateinit var day: Day17

    @BeforeEach
    fun setUp() {
        day = Day17(Input("""target area: x=20..30, y=-10..-5""".trimIndent()))
    }

    @Test
    fun solveLevel1() {
        assertEquals(45, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(112, day.solveLevel2())
    }
}
