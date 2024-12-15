package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 14, dryRun = true) { Day14(it) }

class Day14(val input: Input, val rows: Int = 103, val cols: Int = 101) : Puzzle {

    data class Robot(val p: Point, val v: Point)

    private fun Robot.move(n: Int): Robot {
        val x = Math.floorMod(p.x + v.x * n, cols)
        val y = Math.floorMod(p.y + v.y * n, rows)

        return Robot(Point(
            col = x,
            row = y,
        ), v)
    }

    private val robots = input.lines.map { line ->
        val p = Point.parse(line.substringBetween("p=", " "))
        val v = Point.parse(line.substringAfter("v="))
        Robot(p, v)
    }

    override fun solveLevel1(): Any {
        val moved = robots.map { it.move(100) }
        val quadrants = moved.groupBy { robot ->
            val qx = robot.p.x.compareTo(cols / 2).sign
            val qy = robot.p.y.compareTo(rows / 2).sign
            qx to qy
        }.filterKeys { (qx, qy) -> qx != 0 && qy != 0 }

        return quadrants.values.fold(1) { acc, robots -> acc * robots.size }
    }

    override fun solveLevel2(): Any {
        var robots = robots
        repeat(10000) { i ->
            val grid = Grid(rows, cols) { _, _ -> false }
            var numInEdge = 0
            robots.forEach { r ->
                grid[r.p].value = true
                if (r.p.x !in (5..cols-5)) {
                    numInEdge++
                }
            }
            if (numInEdge < 20) {
                // Tree will be mostly in the center, so only print the ones that don't have many
                // robots on the left/right sides.
                println("$i:")
                println(grid)
                println()
            }
            robots = robots.map { it.move(1) }
        }

        return -1
    }
}
