package y24

import common.puzzle.solvePuzzle
import common.puzzle.Input
import common.puzzle.Puzzle
import common.datastructures.*
import common.ext.*
import common.puzzle.splitToInts
import common.util.*
import java.util.*
import kotlin.math.*
import kotlin.system.exitProcess


fun main() = solvePuzzle(year = 2024, day = 1) { Day1(it) }

class Day1(val input: Input) : Puzzle {
    val left = input.lines
        .map { it.splitToInts(separator = "   ").first() }
        .sorted()
    val right = input.lines
        .map { it.splitToInts(separator = "   ").last() }
        .sorted()

    override fun solveLevel1(): Any {
        return left.mapIndexed { index, l ->
            val r = right[index]
            abs(l - r)
        }.sum()
    }

    override fun solveLevel2(): Any {
        val rightCounts = right.groupingBy { it }.eachCount()
        return left.sumOf { l ->
            val count = rightCounts[l] ?: 0
            l.toLong() * count
        }
    }
}
