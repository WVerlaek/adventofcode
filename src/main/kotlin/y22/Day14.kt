package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.toPoint
import common.datastructures.Cell
import common.datastructures.Grid
import common.datastructures.Point
import common.ext.product
import java.util.PriorityQueue
import kotlin.math.sign


fun main() = solvePuzzle(year = 2022, day = 14) { Day14(it) }

enum class Tile(val c: Char) { Rock('#'), Air('.'), Sand('o') }

data class RockPath(val points: List<Point>)

class CaveSystem(
    val grid: Grid<Tile>,
    val sourceCol: Int,
) {
    fun dropSand(): Boolean {
        var curX = sourceCol
        var curY = 0

        while (curX in 0 until grid.numCols && curY in 0 until grid.numRows - 1) {
            if (grid[curY][curX].value != Tile.Air) {
                return false
            }

            val below = grid[curY + 1][curX]
            if (below.value == Tile.Air) {
                curY++
                continue
            }

            if (curX == 0) {
                // Falls off map on the left.
                return false
            }

            val belowLeft = grid[curY + 1][curX - 1]
            if (belowLeft.value == Tile.Air) {
                curX--
                curY++
                continue
            }

            if (curX == grid.numCols - 1) {
                // Falls off map on the right.
                return false
            }

            val belowRight = grid[curY + 1][curX + 1]
            if (belowRight.value == Tile.Air) {
                curX++
                curY++
                continue
            }

            grid[curY][curX].value = Tile.Sand
            return true
        }

        return false
    }
}

fun parseCaveSystem(lines: List<String>, withFloor: Boolean): CaveSystem {
    val paths = lines.map { line ->
        val points = line.split(" -> ")
        RockPath(points.map(Point::parse))
    }

    val allPoints = paths.flatMap { it.points }
    val minX = allPoints.minOf { it.x }
    val height = allPoints.maxOf { it.y } + 2
    val translate = Point(-minX + height + 1, 0)

    val translatedPaths = paths.map { path ->
        path.copy(path.points.map { it + translate })
    }

    val rows = translatedPaths.flatMap { it.points }.maxOf { it.row } + 3
    val cols = translatedPaths.flatMap { it.points }.maxOf { it.col } + 1 + 2 * (height + 1)

    val grid = Grid(rows, cols) { _, _ -> Tile.Air }
    translatedPaths.forEach { path ->
        for (i in 1 until path.points.size) {
            val from = path.points[i - 1]
            val to = path.points[i]

            val dx = (to - from).x.sign
            val dy = (to - from).y.sign

            var curX = from.x
            var curY = from.y

            while (curX != to.x || curY != to.y) {
                grid[curY][curX].value = Tile.Rock
                curX += dx
                curY += dy
            }
            grid[curY][curX].value = Tile.Rock
        }
    }

    if (withFloor) {
        for (i in 0 until cols) {
            grid[grid.numRows - 1][i].value = Tile.Rock
        }
    }

    grid.cellFormatter = Grid.CellFormatter({ cell -> cell.value.c.toString() }, "")

    val sourceCol = 500 + translate.x
    return CaveSystem(grid, sourceCol)
}

class Day14(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        val cave = parseCaveSystem(input.lines, withFloor = false)

        var count = 0
        while (cave.dropSand()) count++

        println(cave.grid)
        return count
    }

    override fun solveLevel2(): Any {
        val cave = parseCaveSystem(input.lines, withFloor = true)

        var count = 0
        while (cave.dropSand()) count++

        println(cave.grid)
        return count
    }
}
