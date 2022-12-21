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


fun main() = solvePuzzle(year = 2022, day = 21) { Day21(it) }

class Day21(val input: Input) : Puzzle {

    sealed class Job(val id: String) {
        abstract fun evaluate(allJobs: Map<String, Job>): Long
        abstract fun checkUnknown(allJobs: Map<String, Job>, unknown: String): Boolean
        abstract fun solve(allJobs: Map<String, Job>, unknown: String, equals: Long): Long


        class Number(id: String, val n: Int) : Job(id) {
            override fun evaluate(allJobs: Map<String, Job>) = n.toLong()
            override fun checkUnknown(allJobs: Map<String, Job>, unknown: String) = id == unknown
            override fun solve(allJobs: Map<String, Job>, unknown: String, equals: Long): Long {
                if (unknown == id) return equals
                throw IllegalStateException("Should not be reached")
            }
        }

        class Operation(id: String, val left: String, val op: Char, val right: String) : Job(id) {
            var cached: Long? = null

            var isUnknown: Boolean = true
            override fun checkUnknown(allJobs: Map<String, Job>, unknown: String): Boolean {
                if (unknown == id) {
                    isUnknown = true
                    return true
                }

                val l = allJobs.getValue(left).checkUnknown(allJobs, unknown)
                val r = allJobs.getValue(right).checkUnknown(allJobs, unknown)
                isUnknown = l || r
                return isUnknown
            }

            override fun evaluate(allJobs: Map<String, Job>): Long {
                cached?.let { return it }
                val l = allJobs.getValue(left).evaluate(allJobs)
                val r = allJobs.getValue(right).evaluate(allJobs)
                return operator(l, op, r).also { cached = it }
            }

            override fun solve(allJobs: Map<String, Job>, unknown: String, equals: Long): Long {
                val l = allJobs.getValue(left)
                val r = allJobs.getValue(right)

                if (l.checkUnknown(allJobs, unknown)) {
                    // l op r == equals. Solve for l.
                    val rv = r.evaluate(allJobs)
                    val eq = when (op) {
                        '+' -> equals - rv
                        '-' -> equals + rv
                        '*' -> equals / rv
                        '/' -> equals * rv
                        else -> error("unknown operator $op")
                    }
                    return l.solve(allJobs, unknown, eq)
                } else {
                    // Solve for r.
                    val lv = l.evaluate(allJobs)
                    val eq = when (op) {
                        '+' -> equals - lv
                        '-' -> lv - equals
                        '*' -> equals / lv
                        '/' -> lv / equals
                        else -> error("unknown operator $op")
                    }
                    return r.solve(allJobs, unknown, eq)
                }
            }
        }
    }

    fun parseJobs(lines: List<String>): Map<String, Job> {
        return lines.map { line ->
            val id = line.substringBefore(":")
            val job = line.substringAfter(": ")
            val split = job.split(" ")
            val j = when (split.size) {
                1 -> Job.Number(id, split[0].toInt())
                else -> Job.Operation(id, split[0], split[1].first(), split[2])
            }
            id to j
        }.toMap()
    }

    override fun solveLevel1(): Any {
        val jobs = parseJobs(input.lines)
        return jobs["root"]!!.evaluate(jobs)
    }

    override fun solveLevel2(): Any {
        val jobs = parseJobs(input.lines)
        val unknown = "humn"
        val root = jobs["root"]!! as Job.Operation
        root.checkUnknown(jobs, unknown)

        val toSolve: Job
        val equals: Long
        if (jobs[root.left]!!.checkUnknown(jobs, unknown)) {
            // humn is in left subtree.
            toSolve = jobs[root.left]!!
            equals = jobs[root.right]!!.evaluate(jobs)
        } else {
            toSolve = jobs[root.right]!!
            equals = jobs[root.left]!!.evaluate(jobs)
        }
        return toSolve.solve(jobs, unknown, equals)
    }
}
