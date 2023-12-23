@file:Suppress("UnstableApiUsage")

package y23

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.google.common.graph.ValueGraph
import com.google.common.graph.ValueGraphBuilder
import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 23) { Day23(it) }

class Day23(val input: Input) : Puzzle {

    sealed class Tile {
        data object Path : Tile()
        data object Forest : Tile()
        data class Slope(val dir: Dir) : Tile()
    }

    val grid = Grid(input.lines.size, input.lines[0].length) { r, c ->
        when (input.lines[r][c]) {
            '#' -> Tile.Forest
            '.' -> Tile.Path
            '>' -> Tile.Slope(Dir(0, 1))
            'v' -> Tile.Slope(Dir(1, 0))
            '<' -> Tile.Slope(Dir(0, -1))
            '^' -> Tile.Slope(Dir(-1, 0))
            else -> error("Unknown tile")
        }
    }

    fun longestPath(from: Point, to: Point, dist: Int, visited: MutableSet<Point>, canTravelSlope: Boolean): Int {
        if (from == to) {
            return dist
        }

        val curTile = grid[from.row][from.col].value
        val dirs = if (curTile is Tile.Slope && !canTravelSlope) {
            listOf(curTile.dir)
        } else {
            directions
        }

        var maxDist = Int.MIN_VALUE
        for (dir in dirs) {
            val neigh = from + dir.toPoint()
            if (neigh in visited) {
                continue
            }
            if (!grid.withinBounds(neigh.row, neigh.col)) {
                continue
            }
            when (val tile = grid[neigh.row][neigh.col].value) {
                is Tile.Forest -> continue
                is Tile.Path -> {}
                is Tile.Slope -> if (tile.dir != dir && !canTravelSlope) continue
            }

            visited += neigh
            maxDist = maxOf(maxDist, longestPath(neigh, to, dist + 1, visited, canTravelSlope))
            visited -= neigh
        }

        return maxDist
    }

    sealed class Recurse {
        data class Params(val from: Point, val dist: Int) : Recurse()
        data class Undo(val visited: Point) : Recurse()
    }

    fun ValueGraph<Point, Int>.longestPathNonRecursive(from: Point, to: Point): Int {
        val stack = ArrayDeque<Recurse>()
        stack += Recurse.Params(from, 0)

        val visited = mutableSetOf<Point>()
        var maxDist = Int.MIN_VALUE
        while (stack.isNotEmpty()) {
            val recurse = stack.removeLast()
            if (recurse is Recurse.Undo) {
                visited -= recurse.visited
                continue
            }

            val (from, dist) = recurse as Recurse.Params
            if (from == to) {
                maxDist = maxOf(maxDist, dist)
                continue
            }

            visited += from

            for (adj in adjacentNodes(from)) {
                if (adj in visited) {
                    continue
                }
                val edgeDist = edgeValue(from, adj).get()
                stack += Recurse.Undo(adj)
                stack += Recurse.Params(adj, dist + edgeDist)
            }
        }

        return maxDist
    }

    fun Grid<Tile>.toGraph(): ValueGraph<Point, Int> {
        val from = Point(1, 0)
        val to = Point(numCols - 2, numRows - 1)

        val graph = ValueGraphBuilder.undirected()
            .build<Point, Int>()

        graph.addNode(from)
        graph.addNode(to)

        fun Point.neighs() = directions.mapNotNull { dir ->
            val neigh = this + dir.toPoint()
            if (!grid.withinBounds(neigh.row, neigh.col)) {
                return@mapNotNull null
            }
            if (grid[neigh.row][neigh.col].value == Tile.Forest) {
                return@mapNotNull null
            }
            return@mapNotNull neigh
        }

        val queue = mutableListOf(from)
        var i = 0
        while (i < queue.size) {
            val crossing = queue[i]
            i++

            crossing.neighs().forEach { neigh ->
                var dist = 1

                var cur = neigh
                var prev = crossing
                while (true) {
                    val curNeighs = cur.neighs()
                    if (curNeighs.size > 2) {
                        // Found crossing.
                        if (cur !in graph.nodes()) {
                            queue += cur
                        }

                        graph.putEdgeValue(crossing, cur, dist)
                        break
                    } else if (curNeighs.size == 1) {
                        // Found dead end, or finish.
                        if (cur == to) {
                            // Finish
                            graph.putEdgeValue(crossing, cur, dist)
                            break
                        } else {
                            // Dead end, don't add edge
                            break
                        }
                    } else {
                        // Single path, go to next.
                        val next = curNeighs.filter { it != prev }.requireSingleElement()
                        prev = cur
                        cur = next
                    }

                    dist++
                }
            }
        }

        return graph
    }

    override fun solveLevel1(): Any {
        return longestPath(
            Point(1, 0),
            Point(grid.numCols - 2, grid.numRows - 1),
            0,
            mutableSetOf(),
            false
        )
    }

    override fun solveLevel2(): Any {
        val graph = grid.toGraph()
        return graph.longestPathNonRecursive(
            Point(1, 0),
            Point(grid.numCols - 2, grid.numRows - 1),
        )
    }
}
