package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*


fun main() = solvePuzzle(year = 2024, day = 18) { Day18(it) }

class Day18(val input: Input, val size: Int = 71, val takeBytes: Int = 1024) : Puzzle {

    private val bytes = input.lines.map { line -> Point.parse(line) }

    data class Dist(val p: Point, val dist: Int, val heur: Int, val prev: Dist?)
    data class Path(val points: Set<Point>)

    private fun constructPath(dist: Dist): Path {
        val path = mutableSetOf<Point>()
        var d: Dist? = dist
        while (d != null) {
            path += d.p
            d = d.prev
        }

        return Path(path)
    }

    private fun Grid<Boolean>.shortestPath(from: Point, to: Point): Path? {
        val queue = PriorityQueue<Dist>(compareBy { it.dist + it.heur })
        val visited = mutableSetOf<Point>()
        queue += Dist(
            p = from,
            dist = 0,
            heur = from.manhattanDistTo(to),
            prev = null,
        )

        while (queue.isNotEmpty()) {
            val next = queue.poll()

            if (next.p in visited) {
                continue
            }
            if (next.p == to) {
                return constructPath(next)
            }
            visited += next.p

            neighbors(this[next.p]).forEach { neigh ->
                if (!neigh.value) {
                    val neighP = neigh.toPoint()
                    queue += Dist(
                        p = neighP,
                        dist = next.dist + 1,
                        heur = neighP.manhattanDistTo(to),
                        prev = next,
                    )
                }
            }
        }

        return null
    }

    override fun solveLevel1(): Any {
        val grid = Grid(size, size) { _, _ -> false }
        bytes.take(takeBytes).forEach { b -> grid[b].value = true }
        return grid.shortestPath(Point(0, 0), Point(size - 1, size - 1))!!
            .points.size - 1
    }

    override fun solveLevel2(): Any {
        val grid = Grid(size, size) { _, _ -> false }
        bytes.take(takeBytes).forEach { b -> grid[b].value = true }

        var lastPath: Path? = null
        for (i in takeBytes until bytes.size) {
            val b = bytes[i]
            grid[b].value = true

            if (lastPath != null && b !in lastPath.points) {
                // lastPath is still a valid path.
                continue
            }

            lastPath = grid.shortestPath(Point(0, 0), Point(size - 1, size - 1))
                ?: return "${b.x},${b.y}"
        }

        error("no byte found that cuts off path")
    }
}
