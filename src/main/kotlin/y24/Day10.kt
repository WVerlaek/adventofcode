package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 10) { Day10(it) }

class Day10(val input: Input) : Puzzle {

    private fun trails(grid: Grid<Int>, p: Cell<Int>): List<Point> {
        if (p.value == 9) {
            return listOf(p.toPoint())
        }

        return grid.neighbors(p)
            .filter { it.value == p.value + 1 }
            .flatMap { trails(grid, it) }
    }

    private val grid = Grid(input.lines.size, input.lines[0].length) { row, col -> input.lines[row][col].digitToInt() }
    private val trailHeads = grid.cells().filter { it.value == 0 }

    override fun solveLevel1(): Any {
        return trailHeads.sumOf { trails(grid, it).distinct().size }
    }

    override fun solveLevel2(): Any {
        return trailHeads.sumOf { trails(grid, it).size }
    }
}
