package y25

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.collections.forEach
import kotlin.collections.set
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2025, day = 12) { Day12(it) }

private typealias Shape = Grid<Boolean>

class Day12(val input: Input) : Puzzle {

    data class Region(
        val width: Int,
        val height: Int,
        val shapes: List<Int>,
    )

    fun parseInput(): Pair<List<Shape>, List<Region>> {
        val regionsStart = input.lines.indexOfFirst { "x" in it }

        // Each shape is 3x3
        val shapes = (0 until regionsStart / 5).map { i ->
            Grid(input.lines.subList(i * 5 + 1, i * 5 + 4)) { _, _, c ->
                c == '#'
            }
        }

        val regions = input.lines.subList(regionsStart, input.lines.size).map { line ->
            val (size, shapes) = line.split(": ")
            val (w, h) = size.split("x").map { it.toInt() }
            val shapeIds = shapes.split(" ").map { it.toInt() }
            Region(w, h, shapeIds)
        }

        return shapes to regions
    }

    private fun fits(
        region: Region,
        shapes: List<Shape>
    ): Boolean {
        val grid = Grid(rows = region.height, cols = region.width) { _, _ -> false }
        val toPlace = region.shapes.flatMapIndexed { index, count ->
            List(count) { shapes[index] }
        }
        return fits(grid, 0, toPlace.map { it.permutations() }, MutableList(region.width) { 0 })
    }

    private fun fits(
        grid: Grid<Boolean>,
        i: Int,
        toPlacePermutations: List<List<Shape>>,
        fromRow: MutableList<Int>,
    ): Boolean {
        if (i >= toPlacePermutations.size) {
            return true
        }

        val remainingSpace = (grid.numCols * grid.numRows) - fromRow.sum()
        val remainingShapes = toPlacePermutations.size - i
        if (remainingShapes * 7 > remainingSpace) {
            // 7 is heuristic (77% of shape area 9 [3x3]). Assumes we cannot compact
            // better than 77%.
            return false
        }

        // Shapes are 3x3
        val permutations = toPlacePermutations[i]
        permutations.forEach { shape ->
            for (col in 0 until grid.numCols - 2) {
                if (tryPlaceInCol(grid, i, toPlacePermutations, col, fromRow, shape)) {
                    return true
                }
            }
        }

        return false
    }

    private fun Shape.permutations(): List<Shape> {
        val result = mutableListOf<Shape>()
        var shape = this
        for (rotation in 0..<4) {
            result += shape
            result += shape.flipHorizontal()

            if (rotation < 3) {
                shape = shape.rotate90()
            }
        }

        return result.distinct()
    }

    private fun tryPlaceInCol(
        grid: Grid<Boolean>,
        i: Int,
        toPlacePermutations: List<List<Shape>>,
        col: Int,
        fromRow: MutableList<Int>,
        shape: Shape,
    ): Boolean {
        // For this column, try the first row where it can be placed, and return.
        // Don't try later rows as this is never more optimal.
        val fromRowForShape = (0..<3).map { idx -> fromRow[col + idx] }
        val startRow = fromRowForShape.min()
        for (row in startRow until grid.numRows - 2) {
            if (grid.canPlace(shape, row, col)) {
                grid.place(shape, row, col)

                (0..<3).forEach { idx ->
                    fromRow[col + idx] = row
                }
                if (fits(grid, i + 1, toPlacePermutations, fromRow)) {
                    return true
                }

                grid.remove(shape, row, col)
                (0..<3).forEach { idx ->
                    fromRow[col + idx] = fromRowForShape[idx]
                }

                // Found first row it could be placed in, but is unsolvable
                // afterwards, don't try later rows.
                return false
            }
        }

        return false
    }

    private fun Grid<Boolean>.canPlace(shape: Shape, atRow: Int, atCol: Int): Boolean {
        for (dr in 0 until shape.numRows) {
            for (dc in 0 until shape.numCols) {
                val r = atRow + dr
                val c = atCol + dc
                if (this[r][c].value && shape[dr][dc].value) {
                    return false
                }
            }
        }

        return true
    }

    private fun Grid<Boolean>.place(shape: Shape, atRow: Int, atCol: Int) {
        for (dr in 0 until shape.numRows) {
            for (dc in 0 until shape.numCols) {
                val r = atRow + dr
                val c = atCol + dc
                if (shape[dr][dc].value) {
                    this[r][c].value = true
                }
            }
        }
    }

    private fun Grid<Boolean>.remove(shape: Shape, atRow: Int, atCol: Int) {
        for (dr in 0 until shape.numRows) {
            for (dc in 0 until shape.numCols) {
                val r = atRow + dr
                val c = atCol + dc
                if (shape[dr][dc].value) {
                    this[r][c].value = false
                }
            }
        }
    }

    override fun solveLevel1(): Any {
        val (shapes, regions) = parseInput()
        return regions.count {
            fits(it, shapes)
        }
    }

    override fun solveLevel2(): Any {
        TODO()
    }
}
