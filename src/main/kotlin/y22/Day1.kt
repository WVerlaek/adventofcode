package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(year = 2022, day = 1, level = 1) { Day1(it) }

class Day1(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        var max = 0
        var cur = 0
        input.lines.forEach { line ->
            if (line.isEmpty()) {
                if (cur > max) {
                    max = cur
                }
                cur = 0
            } else {
                cur += line.toInt()
            }
        }
        return max
    }

    override fun solveLevel2(): Any {
        TODO()
    }
}
