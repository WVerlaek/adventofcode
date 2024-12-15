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


fun main() = solvePuzzle(year = 2024, day = 15) { Day15(it) }

class Day15(val input: Input) : Puzzle {

    sealed class Tile {
        data object Wall : Tile()
        data class Box(val left: Boolean) : Tile()
        data object Empty : Tile()
    }

    data class Parsed(
        val grid: Grid<Tile>,
        val robot: Point,
        val moves: List<Dir>,
    )

    private fun parseInput(lines: List<String>): Parsed {
        val emptyLine = lines.indexOf("")
        var robot: Point? = null
        val grid = Grid(lines.subList(0, emptyLine)) { row, col, c ->
            when (c) {
                '#' -> Tile.Wall
                '.' -> Tile.Empty
                '@' -> {
                    robot = Point(col, row)
                    Tile.Empty
                }
                'O' -> Tile.Box(true)
                else -> error("unknown tile $c")
            }
        }

        val moves = lines.subList(emptyLine + 1, lines.size).flatMap {
            it.map { c ->
                when (c) {
                    '<' -> directions[0]
                    '^' -> directions[1]
                    '>' -> directions[2]
                    'v' -> directions[3]
                    else -> error("unknown direction $c")
                }
            }
        }

        val r = robot ?: error("no robot found")
        return Parsed(grid, r, moves)
    }

    private fun tryMove(grid: Grid<Tile>, p: Point, dir: Dir): Boolean {
        val neigh = grid[p + dir.toPoint()]
        if (neigh.value == Tile.Wall) {
            return false
        }

        if (neigh.value is Tile.Box) {
            if (!tryMove(grid, neigh.toPoint(), dir)) {
                return false
            }
        }

        neigh.value = grid[p].value
        grid[p].value = Tile.Empty
        return true
    }

    private fun tryMove2(grid: Grid<Tile>, p: Point, dir: Dir): Boolean {
        if (!move2(grid, p, dir, dryRun = true)) {
            return false
        }

        return move2(grid, p, dir, dryRun = false)
    }

    private fun move2(grid: Grid<Tile>, p: Point, dir: Dir, dryRun: Boolean): Boolean {
        val toMove = when (val t = grid[p].value) {
            Tile.Empty -> listOf(p) // Robot
            Tile.Box(left = true) -> listOf(p + Point(1, 0), p)
            Tile.Box(left = false) -> listOf(p + Point(-1, 0), p)
            else -> error("unexpected tile $t")
        }

        return toMove.all { point ->
            val neigh = grid[point + dir.toPoint()]
            if (neigh.value == Tile.Wall) {
                return@all false
            }

            if (neigh.value is Tile.Box) {
                val neighIsLeftBox = (neigh.value as Tile.Box).left
                // Prevent recursion, this is the left side pushing into the right side to the right (or vice versa).
                // We will already check this move as part of the right side of the box.
                val skip = (dir == Dir(0, 1) && !neighIsLeftBox) ||
                        (dir == Dir(0, -1) && neighIsLeftBox)
                if (!skip && !move2(grid, neigh.toPoint(), dir, dryRun)) {
                    return@all false
                }
            }

            if (dryRun) {
                return@all true
            }

            neigh.value = grid[point].value
            grid[point].value = Tile.Empty
            return@all true
        }
    }

    private fun expand(grid: Grid<Tile>): Grid<Tile> {
        return Grid(rows = grid.numRows, cols = grid.numCols * 2) { row, col ->
            val v = grid[row][col / 2].value
            if (v is Tile.Box) {
                v.copy(left = col % 2 == 0)
            } else {
                v
            }
        }
    }

    private fun Grid<Tile>.gpsSum(): Int {
        return cells().sumOf { (row, col, value) ->
            when (value) {
                Tile.Box(true) -> 100 * row + col
                else -> 0
            }
        }
    }

    override fun solveLevel1(): Any {
        val (grid, robotInitial, moves) = parseInput(input.lines)

        var robot = robotInitial
        moves.forEach { dir ->
            if (tryMove(grid, robot, dir)) {
                robot += dir.toPoint()
            }
        }

        return grid.gpsSum()
    }

    override fun solveLevel2(): Any {
        val (grid, robotInitial, moves) = parseInput(input.lines)
        val expandedGrid = expand(grid)
        var robot = robotInitial.copy(col = robotInitial.col * 2)
        moves.forEach { dir ->
            if (tryMove2(expandedGrid, robot, dir)) {
                robot += dir.toPoint()
            }

            expandedGrid.cellFormatter = Grid.CellFormatter({ cell ->
                if (cell.toPoint() == robot) return@CellFormatter "@"
                when (cell.value) {
                    Tile.Wall -> "#"
                    Tile.Empty -> "."
                    Tile.Box(true) -> "["
                    Tile.Box(false) -> "]"
                    else -> error("")
                }
            }, "")
            // println(expandedGrid)
        }

        return expandedGrid.gpsSum()
    }
}
