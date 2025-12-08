package y25

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2025, day = 8) { Day8(it) }

class Day8(val input: Input) : Puzzle {

    private val boxes = input.lines.map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        Point3(x, y, z)
    }

    override fun solveLevel1(): Any {
        return solveLevel1(n = 1000)
    }

    private data class Distance(
        val from: Point3,
        val to: Point3,
        val distanceSquared: Long
    )

    private fun initQueue(boxes: List<Point3>): PriorityQueue<Distance> {
        val queue = PriorityQueue<Distance>(compareBy { it.distanceSquared })
        for (i in 0 until boxes.size) {
            for (j in i + 1 until boxes.size) {
                val from = boxes[i]
                val to = boxes[j]
                queue += Distance(from, to, from.distSquaredTo(to))
            }
        }

        return queue
    }

    fun solveLevel1(n: Int): Any {
        val queue = initQueue(boxes)
        val disjointSet = DisjointSets<Point3>()
        boxes.forEach { disjointSet.find(it) }

        repeat(n) {
            val next = queue.poll()
            if (disjointSet.find(next.from) != disjointSet.find(next.to)) {
                // Connect together
                disjointSet.union(next.from, next.to)
            }
        }

        return disjointSet.sets.values
            .map { it.size }
            .sortedDescending()
            .also { println(it) }
            .take(3)
            .product()
    }

    override fun solveLevel2(): Any {
        val queue = initQueue(boxes)
        val disjointSet = DisjointSets<Point3>()
        boxes.forEach { disjointSet.find(it) }

        while (true) {
            val next = queue.poll()
            if (disjointSet.find(next.from) != disjointSet.find(next.to)) {
                // Connect together
                disjointSet.union(next.from, next.to)

                if (disjointSet.sets.size == 1) {
                    // Last connection
                    return next.from.x * next.to.x.toLong()
                }
            }
        }
    }
}
