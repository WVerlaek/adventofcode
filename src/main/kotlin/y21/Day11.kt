package y21

import common.datastructures.Cell
import common.datastructures.Grid
import common.ext.repeatRun
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import java.util.*

fun main() = solvePuzzle(2021, 11, 2) { Day11(it) }

class Day11(val input: Input) : Puzzle {
    val startingGrid = Grid(input.lines.size, input.lines[0].length) { r, c -> input.lines[r][c].digitToInt()}

    fun Grid<Int>.simulateStep(): Pair<Grid<Int>, Int> {
        val new = copy()
        var flashes = 0
        val queue = LinkedList<Cell<Int>>()

        fun incCell(cell: Cell<Int>) {
            cell.value++
            if (cell.value == 10) {
                flashes++
                new.neighbors(cell, true).forEach { neigh ->
                    queue += neigh
                }
            }
        }

        new.cells().forEach(::incCell)

        while (queue.isNotEmpty()) {
            val cell = queue.pop()
            incCell(cell)
        }

        new.cells().forEach { cell -> if (cell.value >= 10) cell.value = 0 }

        return new to flashes
    }

    override fun solveLevel1(): Any {
        val steps = 100
        var totalFlashes = 0L
        startingGrid.repeatRun(steps) {
            val (next, flashes) = simulateStep()
            totalFlashes += flashes
            next
        }

        return totalFlashes
    }

    override fun solveLevel2(): Any {
        var grid = startingGrid
        var step = 1
        while (true) {
            val (next, flashes) = grid.simulateStep()
            if (flashes == 100) {
                return step
            }
            grid = next
            step++
        }
    }
}
