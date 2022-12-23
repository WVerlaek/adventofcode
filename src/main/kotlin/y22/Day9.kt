package y22

import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import common.ext.intersectAll
import common.ext.removeLast
import common.datastructures.Dir
import common.datastructures.Grid
import common.datastructures.directions
import common.datastructures.fitGrid
import common.datastructures.Point
import common.datastructures.toPoint
import kotlin.math.sign

fun main() = solvePuzzle(year = 2022, day = 9) { Day9(it) }

data class Rope(
    val knots: List<Point>,
) {
    constructor(size: Int) : this(Array<Point>(size) { Point.Zero }.toList() )

    val tail: Point
        get() = knots.last()

    fun move(direction: Dir): Rope {
        val moved = knots.toMutableList()

        moved[0] += direction.toPoint()
        for (i in 1 until knots.size) {
            val moveTo = moved[i - 1]
            val knot = moved[i]
            val dRow = moveTo.row - knot.row
            val dCol = moveTo.col - knot.col
            val doMove = Math.abs(dRow) > 1 || Math.abs(dCol) > 1
            if (!doMove) {
                continue
            }

            moved[i] = knot.copy(
                row = knot.row + dRow.sign,
                col = knot.col + dCol.sign,
            )
        }

        return Rope(moved)
    }

    override fun toString(): String {
        val grid = knots.fitGrid()
        return grid.toString()
    }
}

val dirs = listOf("L", "D", "R", "U")

class Day9(val input: Input) : Puzzle {
    fun parseMoves(lines: List<String>): List<Dir> {
        val moves = mutableListOf<Dir>()
        lines.forEach { line ->
            println(line)
            val (d, n) = line.split(" ")
            val dir = directions[dirs.indexOf(d)]
            repeat(n.toInt()) {
                moves += dir
            }
        }
        return moves
    }

    fun simulateRope(startRope: Rope, moves: List<Dir>): Int {
        val visited = mutableSetOf<Point>()
        var rope = startRope
        moves.forEach { dir ->
            rope = rope.move(dir)
            visited += rope.tail
        }
        return visited.size
    }

    override fun solveLevel1(): Any {
        var rope = Rope(2)
        return simulateRope(rope, parseMoves(input.lines))
    }

    override fun solveLevel2(): Any {
        var rope = Rope(10)
        return simulateRope(rope, parseMoves(input.lines))
    }
}
