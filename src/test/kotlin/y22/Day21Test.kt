package y22

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {
    private val sample = Input("""
    root: pppw + sjmn
    dbpl: 5
    cczh: sllz + lgvd
    zczc: 2
    ptdq: humn - dvpt
    dvpt: 3
    lfqf: 4
    humn: 5
    ljgn: 2
    sjmn: drzm * dbpl
    sllz: 4
    pppw: cczh / lfqf
    lgvd: ljgn * ptdq
    drzm: hmdt - zczc
    hmdt: 32
    """.trimIndent())

    private val day = Day21(sample)

    @Test
    fun solveLevel1() {
        assertEquals(152L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(301L, day.solveLevel2())
    }
}
