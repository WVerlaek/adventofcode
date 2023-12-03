package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {
    private val sample = Input("""
    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...${'$'}.*....
    .664.598..
    """.trimIndent())

    private val day = Day3(sample)

    @Test
    fun solveLevel1() {
        assertEquals(4361, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(467835, day.solveLevel2())
    }
}
