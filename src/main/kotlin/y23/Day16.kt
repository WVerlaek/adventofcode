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


fun main() = solvePuzzle(year = 2023, day = 16) { Day16(it) }

class Day16(val input: Input) : Puzzle {
    private fun parseGrid(lines: List<String>) = Grid(lines.size, lines[0].length) { r, c -> lines[r][c] }

    data class Beam(val row: Int, val col: Int, val dir: Dir)

    private fun Beam.move(grid: Grid<Char>): List<Beam> {
        return when (val c = grid[row][col].value) {
            '.' -> listOf(copy(row = row + dir.dRow, col = col + dir.dCol))
            '|' -> if (dir.dCol != 0) {
                // Split
                listOf(
                    copy(row = row - 1, dir = Dir(-1, 0)),
                    copy(row = row + 1, dir = Dir(1, 0)),
                )
            } else {
                listOf(copy(row = row + dir.dRow))
            }
            '-' -> if (dir.dRow != 0) {
                // Split
                listOf(
                    copy(col = col - 1, dir = Dir(0, -1)),
                    copy(col = col + 1, dir = Dir(0, 1)),
                )
            } else {
                listOf(copy(col = col + dir.dCol))
            }
            '/' -> {
                listOf(copy(row = row - dir.dCol, col = col - dir.dRow, dir = Dir(-dir.dCol, -dir.dRow)))
            }
            '\\' -> {
                listOf(copy(row = row + dir.dCol, col = col + dir.dRow, dir = Dir(dir.dCol, dir.dRow)))
            }
            else -> error("Unknown tile $c")
        }.filter {
            it.row in 0 until grid.numRows && it.col in 0 until grid.numCols
        }
    }

    private fun numEnergized(grid: Grid<Char>, initialBeams: List<Beam>): Int {
        val energized = mutableSetOf<Point>()
        val seenBeams = mutableSetOf<Beam>()

        var beams = initialBeams
        while (beams.isNotEmpty()) {
            beams.forEach { energized += Point(it.col, it.row) }
            beams = beams
                .flatMap { it.move(grid) }
                .filter { it !in seenBeams }
            seenBeams += beams
        }

        return energized.size
    }

    override fun solveLevel1(): Any {
        val grid = parseGrid(input.lines)
        return numEnergized(grid, listOf(Beam(0, 0, Dir(0, 1))))
    }

    override fun solveLevel2(): Any {
        val grid = parseGrid(input.lines)
        val starts = (0 until grid.numRows).flatMap { row ->
            listOf(
                Beam(row, 0, Dir(0, 1)),
                Beam(row, grid.numCols - 1, Dir(0, -1)),
            )
        } + (0 until grid.numCols).flatMap { col ->
            listOf(
                Beam(0, col, Dir(1, 0)),
                Beam(grid.numRows - 1, col, Dir(-1, 0)),
            )
        }

        return starts.maxOf { start ->
            numEnergized(grid, listOf(start))
        }
    }
}
