package y21
 import common.puzzle.Input
 import org.junit.jupiter.api.BeforeEach
 import org.junit.jupiter.api.Test
 import kotlin.test.assertEquals
 
 internal class Day25Test {
     private lateinit var day: Day25
 
     @BeforeEach
     fun setUp() {
         day = Day25(Input("""
             v...>>.vv>
             .vv>>.vv..
             >>.>v>...v
             >>v>>.>.v.
             v>v.vv.v..
             >.>>..v...
             .vv..>.>v.
             v.v..>>v.v
             ....v..v.>
         """.trimIndent()))
     }
 
     @Test
     fun solveLevel1() {
         assertEquals(58, day.solveLevel1())
     }
 }
