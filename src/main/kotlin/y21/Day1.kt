package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 1, 2) { Day1(it) }

class Day1(val input: Input) : Puzzle {
    private val numbers = input.linesToInts()

    override fun solveLevel1(): Any {
        return numbers.numIncreases()
    }

    override fun solveLevel2(): Any {
        val numWindows = numbers.size - 2
        val windows = (0 until numWindows).map { i -> numbers.subList(i, i+3).sum() }
        return windows.numIncreases()
    }
}

private fun List<Int>.numIncreases(): Int {
    data class Acc(val prevVal: Int, val numIncreases: Int)
    return this.fold(Acc(Int.MAX_VALUE, 0)) { acc, v ->
        if (v > acc.prevVal) {
            acc.copy(prevVal = v, numIncreases = acc.numIncreases + 1)
        } else {
            acc.copy(prevVal = v)
        }
    }.numIncreases
}
