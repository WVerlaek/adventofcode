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


fun main() = solvePuzzle(year = 2024, day = 4) { Day4(it) }

class Day4(val input: Input) : Puzzle {

    override fun solveLevel1(): Any {
        val grid = Grid(input.lines.size, input.lines[0].length) { r, c ->
            input.lines[r][c]
        }

        return grid.cells()
            .sumOf { cell ->
                grid.countAt(cell.row, cell.col)
            }
    }

    private fun Grid<Char>.countAt(row: Int, col: Int, text: String = "XMAS"): Int {
        return directionsWithDiagonals.count { dir ->
            var r = row
            var c = col
            text.all { ch ->
                if (!withinBounds(r, c)) return@count false
                val matches = this[r][c].value == ch

                r += dir.dRow
                c += dir.dCol
                matches
            }
        }
    }

    private fun Grid<Char>.isXMas(row: Int, col: Int): Boolean {
        if (this[row][col].value != 'A') return false

        if (row == 0 || row == numRows - 1 || col == 0 || col == numCols - 1) return false

        return setOf(this[row - 1][col - 1].value, this[row + 1][col + 1].value) == setOf('M', 'S') &&
                setOf(this[row - 1][col + 1].value, this[row + 1][col - 1].value) == setOf('M', 'S')
    }

    override fun solveLevel2(): Any {
        val grid = Grid(input.lines.size, input.lines[0].length) { r, c ->
            input.lines[r][c]
        }

        return grid.cells()
            .count { cell ->
                grid.isXMas(cell.row, cell.col)
            }
    }
}
