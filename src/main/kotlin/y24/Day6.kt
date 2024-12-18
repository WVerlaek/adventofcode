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


fun main() = solvePuzzle(year = 2024, day = 6, dryRun = true) { Day6(it) }

class Day6(val input: Input) : Puzzle {

    private fun parseInput(lines: List<String>): Pair<Grid<Boolean>, Point> {
        var guard: Point? = null
        val grid = Grid(lines.size, lines[0].length) { row, col ->
            when (val c = lines[row][col]) {
                '.' -> false
                '#' -> true
                '^' -> {
                    guard = Point(col, row)
                    false
                }
                else -> error("unknown cell value $c at $row, $col")
            }
        }

        val g = guard ?: error("did not find guard")
        return grid to g
    }

    data class PathResult(
        val points: Set<Point>,
        val loop: Boolean,
        val jumpTable: Map<Pair<Point, Dir>, Point>,
    )

    private fun findPath(grid: Grid<Boolean>, start: Point, jumpTable: Map<Pair<Point, Dir>, Point>? = null): PathResult {
        var cur = start
        val visited = mutableSetOf<Point>()
        val visitedWithDir = mutableSetOf<Pair<Point, Dir>>()

        val computeNewJumpTable = jumpTable == null
        val newJumpTable = mutableMapOf<Pair<Point, Dir>, Point>()

        var jumpStart: Point? = null
        var dir = 1
        while (grid.withinBounds(cur.row, cur.col)) {
            visited += cur
            if (!visitedWithDir.add(cur to directions[dir])) {
                return PathResult(visited, true, newJumpTable)
            }

            if (computeNewJumpTable) {
                if (jumpStart == null) {
                    jumpStart = cur
                }
            }

            // Assumes no bad input that could lead to an infinite loop
            while (true) {
                val d = directions[dir]

                val next = jumpTable?.let { it[cur to d] } ?: (cur + d.toPoint())
                if (!grid.withinBounds(next.row, next.col)) {
                    cur = next
                    break
                }

                if (grid[next.row][next.col].value) {
                    // Obstacle, rotate
                    if (computeNewJumpTable) {
                        if (jumpStart != null && jumpStart != cur) {
                            newJumpTable[jumpStart to d] = cur
                        }
                        jumpStart = null
                    }

                    dir = (dir + 1) % 4
                    continue
                }

                cur = next
                break
            }
        }

        return PathResult(visited, false, newJumpTable)
    }

    override fun solveLevel1(): Any {
        val (grid, guard) = parseInput(input.lines)
        return findPath(grid, guard).points.size
    }

    override fun solveLevel2(): Any {
        val (grid, guard) = parseInput(input.lines)

        val normalPath = findPath(grid, guard)

        var numLoops = 0
        for (row in 0 until grid.numRows) {
            for (col in 0 until grid.numCols) {
                val p = Point(col, row)
                if (p == guard) continue
                if (grid[row][col].value) continue

                if (p !in normalPath.points) {
                    // Optimisation: p is never reached, so guard exits map.
                    // Can skip this obstacle.
                    continue
                }

                grid[row][col].value = true
                val withJumpTable = normalPath.jumpTable.filterKeys { (start, _) ->
                    start.row != p.row && start.col != p.col
                }
                val path = findPath(grid, guard, withJumpTable)
                if (path.loop) {
                    numLoops++
                }
                grid[row][col].value = false
            }
        }

        return numLoops
    }
}
