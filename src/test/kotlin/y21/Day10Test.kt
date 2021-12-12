package y21

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {
    private val sample = Input("""
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent())

    private val day = Day10(sample)

    @Test
    fun solveLevel1() {
        assertEquals(26397, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(288957L, day.solveLevel2())
    }
}
