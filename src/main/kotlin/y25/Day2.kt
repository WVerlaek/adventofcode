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


fun main() = solvePuzzle(year = 2025, day = 2) { Day2(it) }

class Day2(val input: Input) : Puzzle {
    data class Range(val start: Long, val end: Long)

    private val ranges = input.string.split(",").map { range ->
        val (start, end) = range.split("-")
        Range(start.toLong(), end.toLong())
    }

    private fun Range.invalidIds(): List<Long> {
        val invalidIds = mutableListOf<Long>()
        var half = start.toString(10).let { str ->
            val halfLen = str.length / 2
            if (halfLen > 0) {
                str.take(halfLen)
            } else {
                "0"
            }
        }
        while (true) {
            val full = "$half$half".toLong()
            if (full > end) {
                return invalidIds
            }
            if (full >= start) {
                invalidIds.add(full)
            }
            half = (half.toLong() + 1).toString()
        }
    }

    private fun Range.invalidIds2(): Set<Long> {
        val invalidIds = mutableSetOf<Long>()
        var base = "1"
        while (true) {
            var sequence = "$base$base"
            if (sequence.toLong() > end) {
                return invalidIds
            }

            while (true) {
                val longValue = sequence.toLong()
                if (longValue > end) {
                    break
                }
                if (longValue >= start) {
                    invalidIds += longValue
                }
                sequence += base
            }

            base = (base.toLong() + 1).toString()
        }
    }

    override fun solveLevel1(): Any {
        return ranges.sumOf { range ->
            range.invalidIds().sum()
        }
    }

    override fun solveLevel2(): Any {
        return ranges.sumOf { range ->
            range.invalidIds2().sum()
        }
    }
}
