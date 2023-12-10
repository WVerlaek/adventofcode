package y23

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2023, day = 10) { Day10(it) }

@Suppress("UnstableApiUsage")
class Day10(val input: Input) : Puzzle {

    data class Coor(val x: Int, val y: Int)

    enum class Tile(val c: Char, val directions: List<Pair<Int, Int>>) {
        Vertical('|', listOf(0 to 1, 0 to -1)),
        Horizontal('-', listOf(-1 to 0, 1 to 0)),
        TopRight('L', listOf(0 to -1, 1 to 0)),
        TopLeft('J', listOf(0 to -1, -1 to 0)),
        BottomLeft('7', listOf(-1 to 0, 0 to 1)),
        BottomRight('F', listOf(0 to 1, 1 to 0)),
        Start('S', listOf()),
        Empty('.', listOf())
    }

    fun parseInput(lines: List<String>): Pair<Graph<Coor>, Coor> {
        val graph = GraphBuilder.directed()
            .build<Coor>()
        var start = Coor(0, 0)

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val node = Coor(x, y)
                val tile = Tile.entries.first { it.c == c }
                tile.directions.forEach { (dx, dy) ->
                    graph.putEdge(node, Coor(x + dx, y + dy))
                }

                if (tile == Tile.Start) {
                    start = node
                }
            }
        }

        return graph to start
    }

    fun cycle(graph: Graph<Coor>, start: Coor): List<Coor> {
        val cycle = mutableListOf<Coor>()
        var cur = start

        val startAdj = graph.predecessors(start)
        var next = startAdj.first { adj -> start in graph.successors(adj) }

        while (next != start) {
            cycle += cur

            val newNext = graph.successors(next)
                .filter { it != cur }
                .requireSingleElement()
            cur = next
            next = newNext
        }
        cycle += cur

        return cycle
    }

    override fun solveLevel1(): Any {
        val (graph, start) = parseInput(input.lines)
        return ceil(cycle(graph, start).size / 2.0).roundToInt()
    }

    private fun floodFill(visited: Grid<Boolean>, from: Coor): List<Coor>? {
        val filled = mutableListOf<Coor>()
        val queue = mutableListOf<Coor>()
        queue += from

        var reachedEdge = false

        var i = 0
        while (i < queue.size) {
            val next = queue[i]
            if (!visited.withinBounds(next.y, next.x)) {
                reachedEdge = true
                i++
                continue
            }
            if (visited[next.y][next.x].value) {
                i++
                continue
            }

            filled += next
            visited[next.y][next.x].value = true

            directions.forEach { (dx, dy) ->
                queue += Coor(next.x + dx, next.y + dy)
            }

            i++
        }

        if (reachedEdge || filled.isEmpty()) {
            return null
        }
        return filled
    }

    fun createGrid(lines: List<String>, cycleCoors: Set<Coor>, startTile: Tile): Grid<Boolean> {
        val grid = Grid(lines.size * 3, lines[0].length * 3) { _, _ -> false }
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if (Coor(col, row) in cycleCoors) {
                    val tile = Tile.entries.first { it.c == c }
                        .let { if (it == Tile.Start) startTile else it }
                    val center = Coor(col * 3 + 1, row * 3 + 1)

                    require(tile.directions.isNotEmpty())
                    grid[center.y][center.x].value = true
                    tile.directions.forEach { (dx, dy) ->
                        grid[center.y + dy][center.x + dx].value = true
                    }
                }
            }
        }

        return grid
    }

    private fun startTile(graph: Graph<Coor>, start: Coor): Tile {
        val adj = graph.predecessors(start)
        require(adj.size == 2)

        val adjDelta = adj.map { (x, y) -> x - start.x to y - start.y }
        return Tile.entries.first { tile -> tile.directions == adjDelta || tile.directions == adjDelta.reversed() }
    }

    override fun solveLevel2(): Any {
        val (graph, start) = parseInput(input.lines)
        val cycle = cycle(graph, start)
        val cycleCoors = cycle.toSet()

        val startTile = startTile(graph, start)
        val grid = createGrid(input.lines, cycleCoors, startTile)

        val fills = listOf(-1 to -1, -1 to 1, 1 to 1, 1 to -1)
            .mapNotNull { (dx, dy) ->
                val adj = Coor(start.x * 3 + 1 + dx, start.y * 3 + 1 + dy)
                floodFill(grid, adj)
            }

        val filledTiles = fills.maxOf { filled ->
            filled.map { (x, y) -> Coor(x / 3, y / 3) }
                .distinct()
                .filter { coor -> coor !in cycleCoors }
                .size
        }

        return filledTiles
    }
}
