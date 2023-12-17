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


fun main() = solvePuzzle(year = 2023, day = 17) { Day17(it) }

class Day17(val input: Input) : Puzzle {

    data class Node(
        val row: Int,
        val col: Int,
        val dir: Dir,
        val numStraight: Int,
    )

    val startNodes = listOf(
        Node(0, 0, Dir(1, 0), 0),
        Node(0, 0, Dir(0, 1), 0))

    val grid = Grid(input.lines.size, input.lines[0].length) { r, c -> input.lines[r][c].digitToInt() }

    operator fun <T> Grid<T>.contains(node: Node): Boolean {
        return withinBounds(node.row, node.col)
    }

    fun minHeatLoss(grid: Grid<Int>, starts: List<Node>, minStraight: Int, maxStraight: Int): Int {
        val visited = mutableSetOf<Node>()

        data class Dist(val node: Node, val dist: Int)

        val queue = PriorityQueue<Dist>(compareBy { it.dist })
        queue += starts.map { Dist(it, 0) }

        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (next.node.row == grid.numRows - 1 && next.node.col == grid.numCols - 1) {
                if (next.node.numStraight < minStraight) {
                    continue
                }
                return next.dist
            }

            if (next.node in visited) {
                continue
            }
            visited += next.node

            fun Node.checkAddToQueue() {
                if (this !in grid) {
                    return
                }
                if (this.numStraight > maxStraight) {
                    return
                }
                queue += Dist(this, next.dist + grid[row][col].value)
            }

            // Left
            if (next.node.numStraight >= minStraight) {
                Node(
                    next.node.row - next.node.dir.dCol,
                    next.node.col + next.node.dir.dRow,
                    Dir(-next.node.dir.dCol, next.node.dir.dRow),
                    1,
                ).checkAddToQueue()
            }

            // Forward
            Node(next.node.row + next.node.dir.dRow,
                next.node.col + next.node.dir.dCol,
                next.node.dir,
                next.node.numStraight + 1,
            ).checkAddToQueue()

            // Right
            if (next.node.numStraight >= minStraight) {
                Node(
                    next.node.row + next.node.dir.dCol,
                    next.node.col - next.node.dir.dRow,
                    Dir(next.node.dir.dCol, -next.node.dir.dRow),
                    1,
                ).checkAddToQueue()
            }
        }

        return Int.MAX_VALUE
    }

    override fun solveLevel1(): Any {
        return minHeatLoss(grid, startNodes, 1, 3)
    }

    override fun solveLevel2(): Any {
        return minHeatLoss(grid, startNodes, 4, 10)
    }
}
