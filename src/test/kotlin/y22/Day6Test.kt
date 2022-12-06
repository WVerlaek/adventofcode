package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {
    private val sample = Input("""
    mjqjpqmgbljsphdztnvjfqwrcgsmlb
    bvwbjplbgvbhsrlpgdmjqwftvncz
    nppdvjthqldpwncqszvftbrmjlhg
    nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg
    zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw
    """.trimIndent())

    private val day = Day6(sample)

    @Test
    fun solveLevel1() {
        assertEquals("7,5,6,10,11", day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals("19,23,23,29,26", day.solveLevel2())
    }
}
