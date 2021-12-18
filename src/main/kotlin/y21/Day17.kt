package y21

import common.datastructures.Point
import common.ext.compareTo
import common.puzzle.Input
import common.puzzle.Puzzle
import common.puzzle.solvePuzzle
import kotlin.math.abs

fun main() = solvePuzzle(2021, 17, 2) { Day17(it) }

data class TargetArea(val x: IntRange, val y: IntRange) {
    companion object {
        fun parse(input: String): TargetArea {
            val split = input.split(", ")
            val xRange = split[0].removePrefix("target area: x=")
            val yRange = split[1].removePrefix("y=")
            val (xFrom, xTo) = xRange.split("..").map { it.toInt() }
            val (yFrom, yTo) = yRange.split("..").map { it.toInt() }
            return TargetArea(xFrom..xTo, yFrom..yTo)
        }
    }

    operator fun contains(p: Point): Boolean {
        return p.x in x && p.y in y
    }
}

class Day17(val input: Input) : Puzzle {
    val targetArea = TargetArea.parse(input.string)

    override fun solveLevel1(): Any {
        var y = 0 // Assuming y >= 0?
        var maxHeight = 0
        val maxX = maxOf(abs(targetArea.x.first), abs(targetArea.x.last))

        var maxYToTry = Int.MAX_VALUE
        val additionalTries = 2000

        outer@ while (y < maxYToTry) {
            // Assuming targetArea x >= 0...
            for (x in 0..maxX) {
                val height = reachesTarget(Point(x, y))
                if (height != null) {
                    maxHeight = maxOf(maxHeight, height)
                    maxYToTry = y + additionalTries
                    break
                }
            }

            y++
        }

        return maxHeight
    }

    fun reachesTarget(initialVelocity: Point): Int? {
        var maxHeight = 0
        var p = Point(0, 0)
        if (initialVelocity == Point.Zero) return if (p in targetArea) 0 else null
        var v = initialVelocity
        // Assuming targetArea x >= 0...
        while (p.x <= targetArea.x && p.y >= targetArea.y) {
            maxHeight = maxOf(maxHeight, p.y)
            if (p in targetArea) {
                return maxHeight
            }

            p += v
            v = Point(maxOf(v.x - 1, 0), v.y - 1)
        }

        return null
    }

    override fun solveLevel2(): Any {
        var y = -10000 // ¯\_(ツ)_/¯
        val maxX = maxOf(abs(targetArea.x.first), abs(targetArea.x.last))

        var maxYToTry = Int.MAX_VALUE
        val additionalTries = 2000

        val initialVelocities = mutableSetOf<Point>()

        outer@ while (y < maxYToTry) {
            // Assuming targetArea x >= 0...
            for (x in 0..maxX) {
                val initialVelocity = Point(x, y)
                if (reachesTarget(initialVelocity) != null) {
                    initialVelocities += initialVelocity
                    maxYToTry = y + additionalTries
                }
            }

            y++
        }

        return initialVelocities.size
    }
}
