package y21

import common.datastructures.Grid
import common.datastructures.Line
import common.datastructures.Point
import common.ext.toOrFrom
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle

fun main() = solvePuzzle(2021, 5, 2) { Day5(it) }

class Day5(val input: Input) : Puzzle {
    val ventLines = input.lines.map { line ->
        val split = line.split(" -> ")
        val p0 = Point.parse(split[0])
        val p1 = Point.parse(split[1])
        Line(p0, p1)
    }

    val width = ventLines.maxOf { line -> maxOf(line.from.col, line.to.col) } + 1
    val height = ventLines.maxOf { line -> maxOf(line.from.row, line.to.row) } + 1
    val grid = Grid(height, width) { _, _ -> 0 }

    init {
        grid.cellFormatter = {
            when (it.value) {
                0 -> "."
                else -> it.value.toString()
            }
        }
    }

    override fun solveLevel1(): Any {
        ventLines.forEach { line ->
            if (line.from.row == line.to.row) {
                // Horizontal line.
                (line.from.col toOrFrom line.to.col)
                    .forEach { i -> grid[line.from.row][i].value++ }
            } else if (line.from.col == line.to.col) {
                // Vertical line.
                (line.from.row toOrFrom line.to.row)
                    .forEach { i -> grid[i][line.from.col].value++ }
            } else {
                // Angled line. Ignore.
            }
        }
        println(grid)
        return grid.cells().count { it.value > 1 }
    }

    override fun solveLevel2(): Any {
        ventLines.forEach { line ->
            if (line.from.row == line.to.row) {
                // Horizontal line.
                (line.from.col toOrFrom line.to.col)
                    .forEach { i -> grid[line.from.row][i].value++ }
            } else if (line.from.col == line.to.col) {
                // Vertical line.
                (line.from.row toOrFrom line.to.row)
                    .forEach { i -> grid[i][line.from.col].value++ }
            } else {
                // Diagonal line.
                if (line.from.row <= line.to.row) {
                    (line.from.row..line.to.row)
                        .forEachIndexed { index, row ->
                            val col = if (line.from.col <= line.to.col) line.from.col + index else line.from.col - index
                            grid[row][col].value++
                        }
                } else {
                    (line.to.row..line.from.row)
                        .forEachIndexed { index, row ->
                            val col = if (line.to.col <= line.from.col) line.to.col + index else line.to.col - index
                            grid[row][col].value++
                        }
                }
            }
        }
        println(grid)
        return grid.cells().count { it.value > 1 }
    }
}
