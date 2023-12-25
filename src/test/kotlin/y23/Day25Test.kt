package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day25Test {
    private val sample = Input("""
    jqt: rhn xhk nvd
    rsh: frs pzl lsr
    xhk: hfx
    cmg: qnr nvd lhk bvb
    rhn: xhk bvb hfx
    bvb: xhk hfx
    pzl: lsr hfx nvd
    qnr: nvd
    ntq: jqt hfx bvb xhk
    nvd: lhk
    lsr: lhk
    rzs: qnr cmg lsr rsh
    frs: qnr lhk lsr
    """.trimIndent())

    private val day = Day25(sample)

    @Test
    fun solveLevel1() {
        assertEquals(54, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(42, day.solveLevel2())
    }
}
