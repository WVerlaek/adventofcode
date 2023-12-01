package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {
    private val sample1 = Input("""
    1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet
    """.trimIndent())

    private val sample2 = Input("""
    two1nine
    eightwothree
    abcone2threexyz
    xtwone3four
    4nineeightseven2
    zoneight234
    7pqrstsixteen
    """.trimIndent())

    private val day1 = Day1(sample1)
    private val day2 = Day1(sample2)

    @Test
    fun solveLevel1() {
        assertEquals(142, day1.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(281, day2.solveLevel2())
    }
}
