package y21

import common.datastructures.Cell
import common.datastructures.Grid
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import java.util.*

fun main() = solvePuzzle(2021, 15, 2) { Day15(it) }

class Day15(val input: Input) : Puzzle {
    private val grid = Grid(input.lines.size, input.lines[0].length) { row, col ->
        input.lines[row][col].digitToInt()
    }

    override fun solveLevel1(): Any {
        return grid.findLowestRiskPath()
    }

    override fun solveLevel2(): Any {
        return grid.expand(5).findLowestRiskPath()
    }

    private fun Grid<Int>.findLowestRiskPath(start: Cell<Int> = this[0][0], end: Cell<Int> = this.rows.last().last()): Int {
        data class Dist(val cell: Cell<Int>, val cost: Int)
        val queue = PriorityQueue<Dist>(compareBy { it.cost })
        val visited = HashSet<Cell<Int>>()
        queue += Dist(start, 0)

        while (queue.isNotEmpty()) {
            val cur = queue.poll()
            if (cur.cell == end) {
                return cur.cost
            }

            if (!visited.add(cur.cell)) {
                // Already visited.
                continue
            }

            neighbors(cur.cell, includeDiagonals = false).forEach { neigh ->
                queue += Dist(neigh, cur.cost + neigh.value)
            }
        }

        throw IllegalStateException("End is unreachable")
    }

    private fun Grid<Int>.expand(times: Int): Grid<Int> {
        return Grid(numRows * times, numCols * times) { row, col ->
            val toAdd = row / numRows + col / numCols
            ((this[row % numRows][col % numCols].value + toAdd - 1) % 9) + 1
        }
    }
}
