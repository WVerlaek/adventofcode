package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {
    private val sample = Input("""
    ???.### 1,1,3
    .??..??...?##. 1,1,3
    ?#?#?#?#?#?#?#? 1,3,1,6
    ????.#...#... 4,1,1
    ????.######..#####. 1,6,5
    ?###???????? 3,2,1
    """.trimIndent())

    private val day = Day12(sample)

    @Test
    fun solveLevel1() {
        assertEquals(21L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(525152L, day.solveLevel2())
    }
}
