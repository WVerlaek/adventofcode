package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {
    private val sample = Input("""
11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
    """.trimIndent())

    private val day = Day2(sample)

    @Test
    fun solveLevel1() {
        assertEquals(1227775554L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(4174379265L, day.solveLevel2())
    }
}
