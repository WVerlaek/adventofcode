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


fun main() = solvePuzzle(year = 2025, day = 5, dryRun = false) { Day5(it) }

class Day5(val input: Input) : Puzzle {
    data class Range(val start: Long, val end: Long)

    val splitIdx = input.lines.indexOf("")

    val ranges = input.lines.subList(0, splitIdx).map { line ->
        val (start, end) = line.split("-")
        Range(start.toLong(), end.toLong())
    }

    val ingredients = input.lines.subList(splitIdx + 1, input.lines.size).map(String::toLong)

    val rangesSortedByStart = ranges.sortedBy { it.start }

    override fun solveLevel1(): Any {
        return ingredients.count { ing ->
            val i = rangesSortedByStart.binarySearch { range ->
                (range.start - ing).sign
            }

            val searchUntilIncl = if (i >= 0) {
                // Found exact match, include it
                i
            } else {
                // Insertion index at -i-1, exclude it
                -i-2
            }
            if (searchUntilIncl < 0) {
                return@count false
            }
            rangesSortedByStart.subList(0, searchUntilIncl + 1).any { range ->
                range.end >= ing
            }
        }
    }

    override fun solveLevel2(): Any {
        var lastTracked = rangesSortedByStart[0].start - 1
        var total = 0L
        rangesSortedByStart.forEach { range ->
            if (range.start > lastTracked) {
                // Track full range
                total += range.end - range.start + 1
                lastTracked = range.end
            } else {
                val n = range.end - maxOf(range.start, lastTracked + 1) + 1
                total += maxOf(n, 0)
                lastTracked = maxOf(lastTracked, range.end)
            }
        }

        return total
    }
}
