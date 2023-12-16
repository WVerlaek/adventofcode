package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test {
    private val sample = Input("""
    .|...\....
    |.-.\.....
    .....|-...
    ........|.
    ..........
    .........\
    ..../.\\..
    .-.-/..|..
    .|....-|.\
    ..//.|....
    """.trimIndent())

    private val day = Day16(sample)

    @Test
    fun solveLevel1() {
        assertEquals(46, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(51, day.solveLevel2())
    }
}
