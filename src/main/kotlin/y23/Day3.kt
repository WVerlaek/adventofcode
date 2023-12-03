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


fun main() = solvePuzzle(year = 2023, day = 3) { Day3(it) }

class Day3(val input: Input) : Puzzle {
    private val grid = Grid(input.lines.size, input.lines[0].length) { r, c ->
        input.lines[r][c]
    }

    data class Number(val n: String, val row: Int, val col: Int) {
        fun isEnginePart(grid: Grid<Char>): Boolean {
            for (r in (row - 1)..(row + 1)) {
                for (c in (col - 1)..(col + n.length)) {
                    if (!grid.withinBounds(r, c)) {
                        continue
                    }

                    val v = grid[r][c].value
                    if (!v.isDigit() && v != '.') {
                        return true
                    }
                }
            }

            return false
        }

        fun toInt() = n.toInt()
    }

    private fun extractNumbers(grid: Grid<Char>): List<Number> {
        val numbers = mutableListOf<Number>()
        grid.rows.forEachIndexed { row, line ->
            var col = 0
            while (col < line.size) {
                if (!line[col].value.isDigit()) {
                    col++
                    continue
                }

                // Found a number.
                val from = col
                while (col < line.size && line[col].value.isDigit()) {
                    col++
                }

                val num = line.subList(from, col)
                    .map { cell -> cell.value }
                    .joinToString("")

                numbers += Number(num, row, from)
            }
        }

        return numbers
    }

    private fun extractNumber(grid: Grid<Char>, row: Int, col: Int): Number {
        var from = col
        while (from >= 0 && grid[row][from].value.isDigit()) {
            from--
        }

        var to = col
        while (to < grid.numCols && grid[row][to].value.isDigit()) {
            to++
        }

        val num = grid[row]
            .subList(from + 1, to)
            .map { it.value }
            .joinToString("")

        return Number(num, row, from + 1)
    }

    data class Gear(val n1: Number, val n2: Number) {
        val ratio = n1.toInt() * n2.toInt()
    }

    private fun extractGears(grid: Grid<Char>): List<Gear> {
        val candidates = grid.cells().filter { it.value == '*' }
        return candidates.mapNotNull { cell ->
            val neighNums = grid.neighbors(cell, includeDiagonals = true)
                .filter { it.value.isDigit() }
                .map { extractNumber(grid, it.row, it.col) }
                .distinct()

            if (neighNums.size != 2) {
                return@mapNotNull null
            }

            return@mapNotNull Gear(neighNums[0], neighNums[1])
        }
    }

    override fun solveLevel1(): Any {
        return extractNumbers(grid)
            .filter { it.isEnginePart(grid) }
            .sumOf { it.toInt() }
    }

    override fun solveLevel2(): Any {
        return extractGears(grid)
            .sumOf { it.ratio }
    }
}
