package y21

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import kotlin.math.abs

fun main() = solvePuzzle(2021, 7, 2) { Day7(it) }

class Day7(val input: Input) : Puzzle {
    val crabs = input.splitToInts()

    override fun solveLevel1(): Any {
        val max = crabs.maxOrNull() ?: 0
        val costs = IntArray(max + 1) { i ->
            crabs.sumOf { crab -> abs(crab - i) }
        }
        return costs.minOrNull() ?: ""
    }

    override fun solveLevel2(): Any {
        val max = crabs.maxOrNull() ?: 0

        val costToMoveNSteps = IntArray(max + 1)
        for (i in 1 until costToMoveNSteps.size) {
            costToMoveNSteps[i] = costToMoveNSteps[i - 1] + i
        }

        val costs = IntArray(max + 1) { i ->
            crabs.sumOf { crab -> costToMoveNSteps[abs(crab - i)] }
        }
        return costs.minOrNull() ?: ""
    }
}
