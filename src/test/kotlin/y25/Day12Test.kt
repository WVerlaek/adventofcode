package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {
    private val sample = Input("""
0:
###
##.
##.

1:
###
##.
.##

2:
.##
###
##.

3:
##.
###
##.

4:
###
#..
###

5:
###
.#.
###

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2
    """.trimIndent())

    private val day = Day12(sample)

    @Test
    fun solveLevel1() {
        assertEquals(2, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        // assertEquals(, day.solveLevel2())
    }
}
