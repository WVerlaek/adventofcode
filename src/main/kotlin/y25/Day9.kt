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


fun main() = solvePuzzle(year = 2025, day = 9) { Day9(it) }

class Day9(val input: Input) : Puzzle {
    private val rectangles = input.lines.map { Point.parse(it) }

    override fun solveLevel1(): Any {
        var max = 0L
        for (i in rectangles.indices) {
            for (j in i + 1 until rectangles.size) {
                val area = abs(rectangles[i].x - rectangles[j].x + 1).toLong() * (rectangles[i].y - rectangles[j].y + 1)
                if (area > max) max = area
            }
        }

        return max
    }

    class CompressedGrid(
        val grid: Grid<Boolean>,
        val xToGridCol: Map<Int, Int>,
        val yToGridRow: Map<Int, Int>,
    ) {
        fun isValid(fromRectangle: Point, toRectangle: Point): Boolean {
            val fromCol = xToGridCol[fromRectangle.x]!!
            val toCol = xToGridCol[toRectangle.x]!!
            val fromRow = yToGridRow[fromRectangle.y]!!
            val toRow = yToGridRow[toRectangle.y]!!

            for (col in fromCol toOrFrom toCol) {
                for (row in fromRow toOrFrom toRow) {
                    if (!grid[row][col].value) {
                        return false
                    }
                }
            }

            return true
        }
    }

    private fun createGrid(rectangles: List<Point>): CompressedGrid {
        val sortedX = rectangles.map { it.x }.distinct().sorted()
        val xToGridCol = rectangles.associate { it.x to sortedX.indexOf(it.x) + 1}

        val sortedY = rectangles.map {it.y }.distinct().sorted()
        val yToGridRow = rectangles.associate { it.y to sortedY.indexOf(it.y) + 1 }

        val numCols = sortedX.size + 2
        val numRows = sortedY.size + 2

        val grid = Grid(numRows, numCols) { _, _ -> false }

        rectangles.indices.forEach { i ->
            val from = rectangles[i]
            val to = rectangles[(i + 1) % rectangles.size]

            val fromCol = xToGridCol[from.x]!!
            val toCol = xToGridCol[to.x]!!
            val fromRow = yToGridRow[from.y]!!
            val toRow = yToGridRow[to.y]!!

            for (col in fromCol toOrFrom toCol) {
                for (row in fromRow toOrFrom toRow) {
                    grid[row][col].value = true
                }
            }
        }

        val outsideCache = mutableSetOf<Point>()
        grid.cells().forEach { cell ->
            grid.neighbors(cell, includeDiagonals = true).forEach { neigh ->
                grid.floodInside(neigh.col, neigh.row, outsideCache)
            }
        }

        return CompressedGrid(grid, xToGridCol, yToGridRow)
    }

    private fun Grid<Boolean>.floodInside(x: Int, y: Int, outsideCache: MutableSet<Point>): Boolean {
        val flooded = mutableListOf<Point>()
        val queue = mutableListOf<Cell<Boolean>>()
        queue += this[y][x]

        var undo = false
        while (queue.isNotEmpty()) {
            val next = queue.removeLast()
            if (next.col == 0 || next.col == numCols - 1 || next.row == 0 || next.row == numRows - 1 || next.toPoint() in outsideCache) {
                // Reached edge, not flooding inside.
                undo = true
                break
            }

            if (next.value) {
                continue
            }

            next.value = true

            flooded += next.toPoint()
            neighbors(next, includeDiagonals = false).forEach { queue += it }
        }

        if (undo) {
            // If we get here, we reached the edge, undo the flood.
            flooded.forEach { p -> this[p.y][p.x].value = false }
            outsideCache += flooded
            return false
        }

        return true
    }

    override fun solveLevel2(): Any {
        val grid = createGrid(rectangles)
        var max = 0L
        for (i in rectangles.indices) {
            for (j in i + 1 until rectangles.size) {
                if (grid.isValid(rectangles[i], rectangles[j])) {
                    val area = (abs(rectangles[i].x - rectangles[j].x) + 1L) * (abs(rectangles[i].y - rectangles[j].y) + 1L)
                    if (area > max) max = area
                }
            }
        }

        return max
    }
}
