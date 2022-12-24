package y22

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2022, day = 24) { Day24(it) }

class Day24(val input: Input) : Puzzle {

    data class Valley(
        val rows: Int,
        val cols: Int,
        val rowBlizzards: List<List<Int>>,
        val colBlizzards: List<List<Int>>,
    ) {
        fun hasBlizzard(t: Int, point: Point): Boolean {
            if (point.x !in 0 until cols || point.y !in 0 until rows) return false
            fun has(t: Int, blizzards: List<Int>, length: Int, offset: Int): Boolean {
                return blizzards.any { b ->
                    val sign = if (b.sign == 0) 1 else b.sign
                    val pos = if (b < 0) -b - 1 else b
                    var blizPos = pos + sign * t
                    while (blizPos < 0) blizPos += length
                    blizPos % length == offset
                }
            }
            return has(t, rowBlizzards[point.y], cols, point.x) || has(t, colBlizzards[point.x], rows, point.y)
        }
    }

    fun parse(lines: List<String>): Valley {
        val rows = lines.size - 2
        val cols = lines[0].length - 2

        val rowBlizzards = Array<MutableList<Int>>(rows) { _ -> mutableListOf() }
        val colBlizzards = Array<MutableList<Int>>(cols) { _ -> mutableListOf() }

        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, ch ->
                val r = row - 1
                val c = col - 1
                when (ch) {
                    '>' -> rowBlizzards[r] += c
                    '<' -> rowBlizzards[r] += -c - 1
                    'v' -> colBlizzards[c] += r
                    '^' -> colBlizzards[c] += -r - 1
                }
            }
        }

        return Valley(rows, cols, rowBlizzards.toList(), colBlizzards.toList())

    }

    // TODO: Shortest path, where t % (cols*rows) is another dimension.

    fun shortestDist(valley: Valley, fromT: Int, start: Point, end: Point): Int {
        data class Visited(val t: Int, val p: Point)
        val visited = mutableSetOf<Visited>()
        data class Dist(val p: Point, val dist: Int)

        val queue = ArrayList<Dist>()
        queue += Dist(start, fromT)

        var i = 0
        while (i < queue.size) {
            try {
                val next = queue[i]
                if (next.p == end) {
                    return next.dist - fromT
                }

                val key = Visited(next.dist % (valley.rows * valley.cols), next.p)
                if (key in visited) {
                    continue
                }
                visited += key

                if (valley.hasBlizzard(next.dist, next.p)) {
                    continue
                }

                var couldMove = false
                directions.forEach { dir ->
                    val neigh = next.p + dir.toPoint()
                    val tryDirection = neigh == start || neigh == end || (neigh.x in 0 until valley.cols && neigh.y in 0 until valley.rows)
                    if (tryDirection) {
                        if (!valley.hasBlizzard(next.dist + 1, neigh)) {
                            queue += Dist(neigh, next.dist + 1)
                            couldMove = true
                        }
                    }
                }

                // if (!couldMove) {
                    // Try standing in place.
                    queue += Dist(next.p, next.dist + 1)
                // }
            } finally {
                i++
            }
        }

        return -1
    }

    override fun solveLevel1(): Any {
        val valley = parse(input.lines)
        val start = Point(0, -1)
        val end = Point(valley.cols - 1, valley.rows)
        return shortestDist(valley, 0, start, end)
    }

    override fun solveLevel2(): Any {
        val valley = parse(input.lines)
        val start = Point(0, -1)
        val end = Point(valley.cols - 1, valley.rows)
        val t1 = shortestDist(valley, 0, start, end)
        val t2 = shortestDist(valley, t1, end, start)
        val t3 = shortestDist(valley, t1+t2, start, end)
        return t1 + t2 + t3
    }
}
