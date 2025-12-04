package y25

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2025, day = 4) { Day4(it) }

class Day4(val input: Input) : Puzzle {

    private val grid = Grid(input.lines) { row, col, c ->
        c == '@'
    }

    private fun neighbourCount() = grid.map { cell ->
        if (cell.value) {
            grid.neighbors(cell, includeDiagonals = true).count { it.value }
        } else {
            null
        }
    }

    private fun Grid<Int?>.removalCandidates(below: Int): List<Cell<Int?>> {
        return cells().filter { cell ->
            val v = cell.value ?: return@filter false
            v < below
        }
    }

    override fun solveLevel1(): Any {
        return neighbourCount().removalCandidates(4).size
    }

    override fun solveLevel2(): Any {
        val countGrid = neighbourCount()
        val queue = ArrayDeque(countGrid.removalCandidates(4))
        var removed = 0
        while (queue.isNotEmpty()) {
            val toRemove = queue.removeFirst()
            removed++

            countGrid.neighbors(toRemove, includeDiagonals = true).forEach { neigh ->
                val v = neigh.value ?: return@forEach
                neigh.value = v - 1
                if (neigh.value == 3) {
                    // Now becomes a removal candidate
                    queue.add(neigh)
                }
            }
        }

        return removed
    }
}
