package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import java.util.*


fun main() = solvePuzzle(year = 2024, day = 16) { Day16(it) }

class Day16(val input: Input) : Puzzle {

    data class Parsed(
        val maze: Grid<Boolean>,
        val start: Point,
        val end: Point,
    )

    private fun parseInput(lines: List<String>): Parsed {
        var start: Point? = null
        var end: Point? = null
        val grid = Grid(lines) { row, col, c ->
            when (c) {
                '#' -> true
                '.' -> false
                'S' -> {
                    start = Point(col, row)
                    false
                }
                'E' -> {
                    end = Point(col, row)
                    false
                }
                else -> error("unknown char $c")
            }
        }

        start ?: error("no start found")
        end ?: error("no end found")
        return Parsed(grid, start!!, end!!)
    }

    data class Dist(
        val p: Point,
        val dir: Dir,
        val cost: Int,
        val prev: Dist?
    )

    data class Path(
        val cost: Int,
        val points: List<Point>,
    )

    private fun constructPath(dist: Dist): Path {
        val path = mutableListOf<Point>()
        var d: Dist? = dist
        while (d != null) {
            path += d.p
            d = d.prev
        }

        return Path(dist.cost, path)
    }

    fun bestPaths(grid: Grid<Boolean>, start: Point, end: Point): List<Path> {
        val queue = PriorityQueue<Dist>(compareBy { it.cost })
        val visited = mutableMapOf<Pair<Point, Dir>, Int>()
        queue += Dist(start, Dir(0, 1), 0, null)

        var bestCost: Int? = null
        val paths = mutableListOf<Path>()

        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (bestCost != null && next.cost > bestCost) {
                // Found all best paths.
                return paths
            }

            if (next.p == end) {
                bestCost = next.cost
                paths += constructPath(next)
                continue
            }

            val key = next.p to next.dir
            if (key in visited && visited.getValue(key) != next.cost) {
                // Already visited at lower cost.
                // If equal cost, use this path as well, it's also a best path.
                continue
            }

            visited[key] = next.cost

            grid.neighbors(grid[next.p]).forEach { neigh ->
                if (neigh.value) {
                    // Wall
                    return@forEach
                }

                val dirToNeigh = (neigh.toPoint() - next.p).toDir()
                val cost = when (dirToNeigh) {
                    next.dir -> {
                        // No change in direction
                        next.cost + 1
                    }

                    (next.dir.toPoint() * -1).toDir() -> {
                        // 180 turn
                        next.cost + 1 + 2000
                    }

                    else -> {
                        // 90 turn
                        next.cost + 1 + 1000
                    }
                }

                queue += Dist(neigh.toPoint(), dirToNeigh, cost, next)
            }
        }

        return paths
    }

    override fun solveLevel1(): Any {
        val (grid, start, end) = parseInput(input.lines)
        return bestPaths(grid, start, end).first().cost
    }

    override fun solveLevel2(): Any {
        val (grid, start, end) = parseInput(input.lines)
        return bestPaths(grid, start, end)
            .flatMap { it.points }
            .distinct()
            .size
    }
}
