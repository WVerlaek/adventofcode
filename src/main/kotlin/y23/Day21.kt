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


fun main() = solvePuzzle(year = 2023, day = 21, dryRun = true) { Day21(it) }

class Day21(val input: Input) : Puzzle {

    private lateinit var start: Point
    private val grid = Grid(input.lines.size, input.lines[0].length) { r, c ->
        when (input.lines[r][c]) {
            '.' -> false
            '#' -> true
            'S' -> {
                start = Point(c, r)
                false
            }
            else -> error("unknown char")
        }
    }

    fun reachablePlots(steps: Int, grid: Grid<Boolean> = this.grid, start: Point = this.start): Int {
        var plots = listOf(start)

        repeat(steps) {
            plots = plots.flatMap { p ->
                directions.mapNotNull { (dx, dy) ->
                    val neigh = Point(p.col + dx, p.row + dy)
                    if (!grid.withinBounds(neigh.row, neigh.col)) {
                        return@mapNotNull null
                    }
                    if (grid[neigh.row][neigh.col].value) {
                        return@mapNotNull null
                    }
                    return@mapNotNull neigh
                }
            }.distinct()
        }

        return plots.size
    }

    override fun solveLevel1(): Any {
        return reachablePlots(64)
    }

    override fun solveLevel2(): Any {
        // 65 <- S -> 65 (131 total)
        // (26501365 - 65) / 131 = 202300

        // start = [65,65]

        // 4 diamond corners
        return reachablePlots(130, grid, Point(0, 65)) +
                reachablePlots(130, grid, Point(130, 65)) +
                reachablePlots(130, grid, Point(65, 0)) +
                reachablePlots(130, grid, Point(65, 130)) +

                // Edges (large area)
                202299L * reachablePlots(131+64, grid, Point(130, 130)) +
                202299L * reachablePlots(131+64, grid, Point(0, 130)) +
                202299L * reachablePlots(131+64, grid, Point(0, 0)) +
                202299L * reachablePlots(131+64, grid, Point(130, 0)) +

                // Edges (small area)
                202300L * reachablePlots(64, grid, Point(130, 130)) +
                202300L * reachablePlots(64, grid, Point(0, 130)) +
                202300L * reachablePlots(64, grid, Point(0, 0)) +
                202300L * reachablePlots(64, grid, Point(130, 0)) +

                // Full squares
                (202299L * 202299L) * reachablePlots(141, grid, start) + // odd
                (202300L * 202300L) * reachablePlots(140, grid, start) // even
    }
}
