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


fun main() = solvePuzzle(year = 2023, day = 22) { Day22(it) }

class Day22(val input: Input) : Puzzle {

    data class Brick(val name: String, var from: Point3, var to: Point3) {
        val minZ get() = minOf(from.z, to.z)

        fun atZ(z: Int): Boolean {
            return z in minOf(from.z, to.z)..maxOf(from.z, to.z)
        }

        fun moveDown() {
            from = from.copy(z = from.z - 1)
            to = to.copy(z = to.z - 1)
        }

        fun allPoints(): List<Point3> {
            val size = from.manhattanDistTo(to) + 1
            val dx = (to.x - from.x).sign
            val dy = (to.y - from.y).sign
            val dz = (to.z - from.z).sign
            return (0 until size).map { i ->
                Point3(from.x + i * dx, from.y + i * dy, from.z + i * dz)
            }
        }

        override fun toString(): String {
            return name
        }
    }

    val bricks = input.lines.mapIndexed { i, line ->
        val (from, to) = line.split("~")
            .map { Point3.parse(it) }
        Brick("${'A' + i}", from, to)
    }

    data class Result(val blockedBy: Map<Brick, List<Brick>>, val supporting: Map<Brick, List<Brick>>)

    fun simulateFall(): Result {
        val sorted = bricks.sortedBy { it.minZ }
        val bricksByXY = mutableMapOf<Point, MutableList<Brick>>()
        bricks.forEach { brick ->
            brick.allPoints().forEach { point ->
                val xy = Point(point.x, point.y)
                bricksByXY[xy] = (bricksByXY[xy] ?: mutableListOf()).also { it.add(brick) }
            }
        }

        bricksByXY.keys.forEach { key ->
            bricksByXY[key] = bricksByXY[key]!!.sortedBy { it.minZ }.toMutableList()
        }

        val blockedBy = mutableMapOf<Brick, List<Brick>>()

        sorted.forEach { brick ->
            // Move brick down as far as possible.
            while (brick.minZ > 1) {
                val blocking = brick.allPoints().mapNotNull { point ->
                    val xy = Point(point.x, point.y)
                    val bricksAtXy = bricksByXY[xy] ?: emptyList()
                    bricksAtXy.find { it.atZ(brick.minZ - 1) }
                }.distinct()

                if (blocking.isNotEmpty()) {
                    blockedBy[brick] = blocking
                    break
                }

                brick.moveDown()
            }
        }

        val supporting = bricks.associateWith { brick ->
            blockedBy.entries.filter { (_, blocked) -> brick in blocked }.map { it.key }
        }

        return Result(blockedBy, supporting)
    }

    override fun solveLevel1(): Any {
        val (blockedBy, supporting) = simulateFall()
        return supporting.count { (_, supp) ->
            supp.all { blockedBy[it]!!.size > 1 }
        }
    }

    fun numFalls(toRemove: Brick, result: Result): Int {
        val (blockedBy, supporting) = result

        val removed = mutableSetOf(toRemove)

        val queue = mutableListOf(toRemove)
        var i = 0
        while (i < queue.size) {
            val brick = queue[i]
            i++

            if (i == 1 || blockedBy[brick]?.all { it in removed } != false) {
                // Brick falls
                removed += brick
                queue += supporting[brick] ?: emptyList()
            }
        }

        return removed.size - 1
    }

    override fun solveLevel2(): Any {
        val result = simulateFall()
        return bricks.sumOf { brick ->
            numFalls(brick, result)
        }
    }
}
