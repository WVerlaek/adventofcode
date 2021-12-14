package y21

import common.puzzle.Input
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day14Test {
    lateinit var day: Day14

    @BeforeEach
    fun setUp() {
        day = Day14(Input("""
            NNCB
            
            CH -> B
            HH -> N
            CB -> H
            NH -> C
            HB -> C
            HC -> B
            HN -> C
            NN -> C
            BH -> H
            NC -> B
            NB -> B
            BN -> B
            BB -> N
            BC -> B
            CC -> N
            CN -> C
        """.trimIndent()))
    }

    @Test
    fun solveLevel1() {
        assertEquals(1588L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(2188189693529L, day.solveLevel2())
    }
}
