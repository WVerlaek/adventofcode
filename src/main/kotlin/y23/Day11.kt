package y23

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 11) { Day11(it) }

class Day11(val input: Input, val deltaOverride: Int? = null) : Puzzle {

    data class Expanded(
        val grid: Grid<Boolean>,
        val expandedRow: List<Int>,
        val expandedCol: List<Int>
    ) {
        fun get(row: Int, col: Int): Boolean {
            return grid[expandedRow[row]][expandedCol[col]].value
        }

        fun expandedPoint(row: Int, col: Int) = Point(expandedCol[col], expandedRow[row])
    }

    fun parseInput(lines: List<String>): Pair<Grid<Boolean>, List<Point>> {
        val galaxies = mutableListOf<Point>()
        val grid = Grid(lines.size, lines[0].length) { r, c ->
            (lines[r][c] == '#').also { isGalaxy ->
                if (isGalaxy) galaxies += Point(c, r)
            }
        }

        return grid to galaxies
    }

    fun Grid<Boolean>.expanded(delta: Int): Expanded {
        var rowExpand = 0
        val expandedRow = List(numRows) { row ->
            if (rows[row].all { !it.value }) {
                // Expand
                rowExpand += delta
            }
            row + rowExpand
        }

        var colExpand = 0
        val expandedCol = List(numCols) { col ->
            if ((0 until numRows).all { row -> !this[row][col].value }) {
                // Expand
                colExpand += delta
            }
            col + colExpand
        }

        return Expanded(this, expandedRow, expandedCol)
    }

    private fun expandedDistsSum(delta: Int): Long {
        val (grid, galaxies) = parseInput(input.lines)
        val expanded = grid.expanded(delta)

        var sum = 0L
        galaxies.forEachIndexed { i, galaxy ->
            for (j in i+1 until galaxies.size) {
                sum += expanded.expandedPoint(galaxy.row, galaxy.col)
                    .manhattanDistTo(expanded.expandedPoint(galaxies[j].row, galaxies[j].col))
            }
        }

        return sum
    }

    override fun solveLevel1(): Any {
        return expandedDistsSum(delta = 1)
    }

    override fun solveLevel2(): Any {
        return expandedDistsSum(delta = deltaOverride ?: 999999)
    }
}
