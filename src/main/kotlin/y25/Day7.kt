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


fun main() = solvePuzzle(year = 2025, day = 7) { Day7(it) }

class Day7(val input: Input) : Puzzle {
    enum class Tile { Empty, Start, Splitter, Beam }

    private fun parseGrid() = Grid(input.lines) { _, _, c ->
        when (c) {
            '.' -> Tile.Empty
            'S' -> Tile.Start
            '^' -> Tile.Splitter
            else -> error("Invalid tile $c")
        }
    }

    private fun Grid<Tile>.placeBeam(row: Int, col: Int): Boolean {
        if (!withinBounds(row, col)) {
            return false
        }

        if (this[row][col].value != Tile.Empty) {
            return false
        }

        this[row][col].value = Tile.Beam
        if (row == numRows - 1) {
            return false
        }

        if (this[row + 1][col].value != Tile.Splitter) {
            return false
        }

        if (col > 0) {
            this[row + 1][col - 1].value = Tile.Beam
        }
        if (col < numCols - 1) {
            this[row + 1][col + 1].value = Tile.Beam
        }

        return true
    }

    override fun solveLevel1(): Any {
        var splits = 0
        val grid = parseGrid()
        grid.rows.forEach { row ->
            row.forEach { (row, col, tile) ->
                when (tile) {
                    Tile.Empty -> {}
                    Tile.Start -> if (grid.placeBeam(row + 1, col)) splits++
                    Tile.Splitter -> {}
                    Tile.Beam -> if (grid.placeBeam(row + 1, col)) splits++
                }
            }
        }

        return splits
    }

    private fun Grid<Tile>.numTimelines(row: Int, col: Int, cache: MutableMap<Point, Long> = mutableMapOf()): Long {
        if (!withinBounds(row, col)) {
            return 1L
        }

        val p = Point(row = row, col = col)
        cache[p]?.let {
            return it
        }

        return when (this[row][col].value) {
            Tile.Empty -> numTimelines(row + 1, col, cache)
            Tile.Start ->  numTimelines(row + 1, col, cache)
            Tile.Beam ->  numTimelines(row + 1, col, cache)
            Tile.Splitter -> {
                numTimelines(row, col - 1, cache) + numTimelines(row, col + 1, cache)
            }
        }.also {
            cache[p] = it
        }
    }

    override fun solveLevel2(): Any {
        val grid = parseGrid()
        return grid.rows[0]
            .first { it.value == Tile.Start }
            .let { (row, col, _ ) ->
                grid.numTimelines(row, col)
            }
    }
}
