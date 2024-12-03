package y24

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {
    private val sample = Input("""
    xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
    """.trimIndent())

    private val day = Day3(sample)

    @Test
    fun solveLevel1() {
        assertEquals(161L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(48L, Day3(Input("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")).solveLevel2())
    }
}
