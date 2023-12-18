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


fun main() = solvePuzzle(year = 2023, day = 18) { Day18(it) }

class Day18(val input: Input) : Puzzle {

    data class Instruction(val dir: Dir, val amount: Int)

    private val instructions = input.lines.map { instr ->
        val (d, n, _) = instr.split(" ")
        val dir = when (d.single()) {
            'U' -> Dir(-1, 0)
            'R' -> Dir(0, 1)
            'D' -> Dir(1, 0)
            'L' -> Dir(0, -1)
            else -> error("Unknown dir")
        }
        Instruction(dir, n.toInt())
    }

    private val invInstructions = input.lines.map { instr ->
        val (_, _, col) = instr.split(" ")
        val color = col.substring(2, col.length - 1)
        val dir = when (color.last().digitToInt()) {
            0 -> Dir(0, 1)
            1 -> Dir(1, 0)
            2 -> Dir(0, -1)
            3 -> Dir(-1, 0)
            else -> error("Unknown dir")
        }
        val dist = color.substring(0, color.length - 1).toInt(16)
        Instruction(dir, dist)
    }

    private fun fill(grid: Grid<Boolean>): Int {
        val visited = mutableSetOf<Point>()
        val queue = mutableListOf<Point>()
        queue += Point(0, 0)

        var i = 0
        while (i < queue.size) {
            val next = queue[i]
            i++

            if (!grid.withinBounds(next.row, next.col)) {
                continue
            }
            if (grid[next.row][next.col].value) {
                continue
            }
            if (next in visited) {
                continue
            }
            visited += next

            directions.forEach { dir ->
                queue += Point(next.col + dir.dCol, next.row + dir.dRow)
            }
        }

        return visited.size
    }

    override fun solveLevel1(): Any {
        val n = 900
        val grid = Grid(n, n) { _, _ -> false }

        var cur = Point(n / 2, n / 2)
        grid[cur.row][cur.col].value = true

        instructions.forEach { instr ->
            for (i in 0 until instr.amount) {
                cur = Point(cur.col + instr.dir.dCol, cur.row + instr.dir.dRow)
                grid[cur.row][cur.col].value = true
            }
        }

        val outside = fill(grid)
        return n*n - outside
    }

    private fun polygonArea(points: List<Point>): Long {
        var area = 0L
        var prev = points.size - 1
        var edge = 0L
        for (i in points.indices) {
            area += (points[prev].col + points[i].col).toLong() * (points[prev].row - points[i].row)

            edge += points[prev].manhattanDistTo(points[i])
            prev = i
        }

        return abs(area / 2) + edge / 2 + 1
    }

    override fun solveLevel2(): Any {
        var start = Point(0, 0)
        val points = invInstructions.map { instr ->
            val next = Point(start.col + instr.dir.dCol * instr.amount, start.row + instr.dir.dRow * instr.amount)
            start = next
            next
        }
        return polygonArea(points)
    }
}
