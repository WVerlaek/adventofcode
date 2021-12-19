package y21

import common.puzzle.Input
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day18Test {
    @Test
    fun testParse() {
        assertEquals(Element.Pair(
            Element.Pair(
                Element.Number(1),
                Element.Number(9),
            ),
            Element.Pair(
                Element.Number(8),
                Element.Number(5),
            ),
        ), Element.parse("[[1,9],[8,5]]"))
    }

    @Test
    fun testReduceSimple() {
        assertReductions(
            "[[[[[4,3],4],4],4],4]",
            "[[[[0,7],4],4],4]",
        )
    }

    @Test
    fun testReduceReversed() {
        assertReductions(
            "[4,[4,[4,[4,[3,4]]]]]",
            "[4,[4,[4,[7,0]]]]",
        )
    }

    @Test
    fun testReduce() {
        assertReductions(
            "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]",
            "[[[[0,7],4],[7,[[8,4],9]]],[1,1]]",
            "[[[[0,7],4],[15,[0,13]]],[1,1]]",
            "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]",
            "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]",
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
        )
    }

    private fun assertReductions(vararg reductions: String) {
        for (i in 0 until reductions.size - 1) {
            val from = reductions[i]
            val to = reductions[i + 1]
            assertEquals(to, Element.parse(from).also { it.reduce() }.toString(), "From: $from")
        }
    }

    @Test
    fun solveLevel1_1() {
        assertEquals(143L, Day18(Input("""
            [1,2]
            [[3,4],5]
        """.trimIndent())).solveLevel1())
    }

    @Test
    fun solveLevel1_2() {
        assertEquals(
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
            (Element.parse("[[[[4,3],4],4],[7,[[8,4],9]]]") + Element.parse("[1,1]")).toString()
        )
        assertEquals(1384L, Day18(Input("""
            [[[[4,3],4],4],[7,[[8,4],9]]]
            [1,1]
        """.trimIndent())).solveLevel1())
    }

    @Test
    fun solveLevel1_3() {
        assertEquals(1137L, Day18(Input("""
            [1,1]
            [2,2]
            [3,3]
            [4,4]
            [5,5]
            [6,6]
        """.trimIndent())).solveLevel1())
    }

    @Test
    fun solveLevel1() {
        assertEquals(4140L, Day18(Input("""
            [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
            [[[5,[2,8]],4],[5,[[9,9],0]]]
            [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
            [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
            [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
            [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
            [[[[5,4],[7,7]],8],[[8,3],8]]
            [[9,3],[[9,9],[6,[4,9]]]]
            [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
            [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
        """.trimIndent())).solveLevel1())
    }

    @Test
    fun solveLevel2() {
        assertEquals(3993L, Day18(Input("""
            [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
            [[[5,[2,8]],4],[5,[[9,9],0]]]
            [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
            [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
            [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
            [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
            [[[[5,4],[7,7]],8],[[8,3],8]]
            [[9,3],[[9,9],[6,[4,9]]]]
            [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
            [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
        """.trimIndent())).solveLevel2())
    }
}
