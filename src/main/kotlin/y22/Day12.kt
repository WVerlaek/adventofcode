package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.toPoint
import common.datastructures.Cell
import common.datastructures.Grid
import common.datastructures.Point
import java.util.PriorityQueue


fun main() = solvePuzzle(year = 2022, day = 12) { Day12(it) }

data class Node(val height: Int) {
    var visited = false
    var dist = Int.MAX_VALUE
}

data class HeightMap(
    val grid: Grid<Node>,
    val start: Point,
    val end: Point,
)

fun parseHeightMap(lines: List<String>): HeightMap {
    lateinit var start: Point
    lateinit var end: Point
    val grid = Grid(lines.size, lines[0].length) { row, col ->
        val h = when (val c = lines[row][col]) {
            'S' -> {
                start = Point(col, row)
                0
            }
            'E' -> {
                end = Point(col, row)
                25
            }
            else -> c - 'a'
        }
        Node(h)
    }

    return HeightMap(grid, start, end)
}

fun Grid<Node>.bfs(start: Point, targets: Set<Point>, canWalk: (fromHeight: Int, toHeight: Int) -> Boolean): Int {
    val queue = ArrayList<Cell<Node>>()
    val startCell = this[start]
    startCell.value.dist = 0
    queue += startCell

    var i = 0
    while (i < queue.size) {
        val p = queue[i]
        if (p.value.visited) {
            i++
            continue
        }
        p.value.visited = true

        if (Point(p.col, p.row) in targets) {
            return p.value.dist
        }

        neighbors(p)
            .filter { neigh -> canWalk(p.value.height, neigh.value.height) }
            .filter { neigh -> neigh.value.dist > p.value.dist + 1 }
            .forEach { neigh ->
                neigh.value.dist = p.value.dist + 1
                queue += neigh
            }
        i++
    }

    return -1
}

class Day12(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        val map = parseHeightMap(input.lines)
        return map.grid.bfs(map.start, setOf(map.end)) { fromHeight, toHeight ->
            toHeight <= fromHeight + 1
        }
    }

    override fun solveLevel2(): Any {
        val map = parseHeightMap(input.lines)
        val targets = map.grid.cells()
            .filter { it.value.height == 0 }
            .map { it.toPoint() }
            .toSet()
        return map.grid.bfs(map.end, targets) { fromHeight, toHeight ->
            fromHeight <= toHeight + 1
        }
    }
}
