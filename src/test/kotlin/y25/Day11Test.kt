package y25

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {
    private val sample = Input("""
aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out
    """.trimIndent())

    private val day = Day11(sample)

    @Test
    fun solveLevel1() {
        assertEquals(5L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(2L, Day11(Input("""
             svr: aaa bbb
             aaa: fft
             fft: ccc
             bbb: tty
             tty: ccc
             ccc: ddd eee
             ddd: hub
             hub: fff
             eee: dac
             dac: fff
             fff: ggg hhh
             ggg: out
             hhh: out
         """.trimIndent())).solveLevel2())
    }
}
