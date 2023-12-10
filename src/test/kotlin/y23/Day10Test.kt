package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {
    private val sample = Input("""
    ..F7.
    .FJ|.
    SJ.L7
    |F--J
    LJ...
    """.trimIndent())
    private val sample2 = Input("""
    FF7FSF7F7F7F7F7F---7
    L|LJ||||||||||||F--J
    FL-7LJLJ||||||LJL-77
    F--JF--7||LJLJ7F7FJ-
    L---JF-JLJ.||-FJLJJ7
    |F|F-JF---7F7-L7L|7|
    |FFJF7L7F-JF7|JL---7
    7-L-JL7||F7|L7F-7F7|
    L.L7LFJ|||||FJL7||LJ
    L7JLJL-JLJLJL--JLJ.L
    """.trimIndent())

    private val day = Day10(sample)

    @Test
    fun solveLevel1() {
        assertEquals(8, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(10, Day10(sample2).solveLevel2())
    }
}
