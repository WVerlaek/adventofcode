package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.size
import java.util.*


fun main() = solvePuzzle(year = 2024, day = 20) { Day20(it) }

// To prevent overflows when adding 2 max values together.
private const val MaxValue = Int.MAX_VALUE / 3

class Day20(val input: Input, val minSave: Int = 100) : Puzzle {
    private lateinit var start: Point
    private lateinit var end: Point

    private val grid = Grid(input.lines) { row, col, c ->
        when (c) {
            '.' -> false
            '#' -> true
            'S' -> {
                start = Point(col, row)
                false
            }
            'E' -> {
                end = Point(col, row)
                false
            }
            else -> error("unknown c $c")
        }
    }

    private fun bfs(from: Point): Map<Point, Int> {
        data class Dist(
            val p: Point,
            val dist: Int,
        )

        val dists = mutableMapOf<Point, Int>()
        val queue = PriorityQueue<Dist>(compareBy { it.dist })
        queue += Dist(from, 0)

        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (next.p in dists) {
                continue
            }

            dists[next.p] = next.dist
            grid.neighbors(grid[next.p]).forEach { neigh ->
                if (!neigh.value) {
                    queue += Dist(neigh.toPoint(), next.dist + 1)
                }
            }
        }

        return dists
    }

    data class Cheat(
        val from: Point,
        val to: Point,
        val savedTime: Int,
    )

    private fun findCheats(maxCheatTime: Int): List<Cheat> {
        val distFromStart = bfs(start)
        val distFromEnd = bfs(end)
        val distWithoutCheats = distFromStart.getValue(end)

        fun findCheats(from: Point): List<Cheat> {
            data class Dist(
                val p: Point,
                val dist: Int,
            )
            val cheats = mutableListOf<Cheat>()
            val queue = PriorityQueue<Dist>(compareBy { it.dist })
            val visited = mutableSetOf<Point>()
            queue += Dist(from, 0)

            while (queue.isNotEmpty()) {
                val next = queue.poll()
                if (next.p in visited) {
                    continue
                }
                visited += next.p
                if (!grid[next.p].value) {
                    // Try ending cheat.
                    val totalDist = distFromStart.getValue(from) + next.dist + distFromEnd.getValue(next.p)
                    val savedTime = distWithoutCheats - totalDist
                    if (savedTime >= minSave) {
                        cheats += Cheat(from, next.p, savedTime)
                    }
                }

                if (next.dist >= maxCheatTime) {
                    continue
                }

                grid.neighbors(grid[next.p]).forEach { neigh ->
                    queue += Dist(neigh.toPoint(), next.dist + 1)
                }
            }

            return cheats
        }

        val cheats = mutableListOf<Cheat>()
        grid.cells()
            .filter { !it.value }
            .forEach { c1 ->
                // Try cheating from c1
                cheats += findCheats(c1.toPoint())
            }

        return cheats
    }


    override fun solveLevel1(): Any {
        return findCheats(2).size
    }

    override fun solveLevel2(): Any {
        return findCheats(20).size
    }
}
