package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.splitToInts
import kotlin.math.*

fun main() = solvePuzzle(year = 2024, day = 2) { Day2(it) }

class Day2(val input: Input) : Puzzle {

    override fun solveLevel1(): Any {
        return input.lines.count {
            isSafe(it.splitToInts(" "), 0, 1)
        }
    }

    private fun isSafe(level: List<Int>, i0: Int, i1: Int, skipIndex: Int = level.size): Boolean {
        if (i1 >= level.size || (i1 >= skipIndex && i1 + 1 >= level.size)) {
            return true
        }

        val first = if (skipIndex == 0) 1 else 0
        val second = if (skipIndex <= 1) 2 else 1
        val sign = (level[second] - level[first]).sign

        val v0 = if (i0 >= skipIndex) level[i0 + 1] else level[i0]
        val v1 = if (i1 >= skipIndex) level[i1 + 1] else level[i1]

        if (areValuesSafe(sign, v0, v1)) {
            return isSafe(level, i0 + 1, i1 + 1, skipIndex)
        }

        return false
    }

    private fun areValuesSafe(sign: Int, val0: Int, val1: Int): Boolean {
        val diff = abs(val1 - val0)
        if (diff == 0 || diff > 3) {
            return false
        }
        return (val1 - val0).sign == sign
    }

    override fun solveLevel2(): Any {
        return input.lines.count {
            val report = it.splitToInts(" ")

            // Try skipping each index, or not skipping at all (index = report.size).
            (0..report.size).any { skipIndex ->
                isSafe(report, 0, 1, skipIndex)
            }
        }
    }
}
