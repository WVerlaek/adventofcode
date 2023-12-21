package y23

import common.puzzle.Input
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {
    private val sample = Input("""
    broadcaster -> a, b, c
    %a -> b
    %b -> c
    %c -> inv
    &inv -> a
    """.trimIndent())

    private val day = Day20(sample)

    @Test
    fun solveLevel1() {
        assertEquals(32000000L, day.solveLevel1())
    }

    @Test
    fun solveLevel2() {
         assertEquals(207652583562007L, day.solveLevel2())
    }

    @Test
    fun solveLevel2_loop_tn() {
        val input = Input("""
            broadcaster -> lf
            %lf -> tn, xv
            &tn -> lf, xv, xm, nn, mz, fc, ng
            %xv -> mz
            %mz -> fc
            %fc -> tt
            %tt -> lp, tn
            %lp -> tn, ng
            %ng -> gp
            %gp -> tn, nn
            %nn -> nv
            %nv -> pd, tn
            %pd -> tn, bm
            %bm -> tn
        """.trimIndent())
        assertEquals(3761, Day20(input).simulateUntil1("xm"))
    }

    @Test
    fun solveLevel2_loop_hd() {
        val input = Input("""
            broadcaster -> qg
            %qg -> sj, hd
            %sj -> ln, hd
            &hd -> bs, gk, tr, qg, ln, cx
            %ln -> ps
            %ps -> hd, sg
            %sg -> gk, hd
            %gk -> bs
            %bs -> lj
            %lj -> hd, cx
            %cx -> bh
            %bh -> hd, cp
            %cp -> sc, hd
            %sc -> hd
        """.trimIndent())
        assertEquals(3739, Day20(input).simulateUntil1("tr"))
    }

    @Test
    fun solveLevel2_loop_vc() {
        val input = Input("""
            broadcaster -> tb
            %tb -> vc, th
            &vc -> tb, mf, dr, th, kk, bk
            %th -> gv
            %gv -> vc, bk
            %bk -> nc
            %nc -> kk, vc
            %kk -> nb
            %nb -> vc, hb
            %hb -> vc, mf
            %mf -> lg
            %lg -> tz, vc
            %tz -> vc, mc
            %mc -> vc
        """.trimIndent())
        assertEquals(3797, Day20(input).simulateUntil1("dr"))
    }

    @Test
    fun solveLevel2_loop_jx() {
        val input = Input("""
            broadcaster -> dv
            %bv -> ml, jx
            %jv -> xc
            %sm -> jv
            %dv -> jx, sm
            %xc -> dx
            %kf -> bv
            %vs -> qm, jx
            %bt -> jx
            &jx -> sm, jv, xc, qm, dv, nh, kf
            %qm -> kf
            %ml -> qb, jx
            %qb -> bt, jx
            %dx -> jx, vs
        """.trimIndent())
        assertEquals(3889, Day20(input).simulateUntil1("nh"))
    }
}
