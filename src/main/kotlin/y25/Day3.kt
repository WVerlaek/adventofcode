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


fun main() = solvePuzzle(year = 2025, day = 3) { Day3(it) }

class Day3(val input: Input) : Puzzle {

    val banks = input.lines.map { line ->
        line.map { it.digitToInt() }
    }

    fun largestJoltage(
        bank: List<Int>,
        i: Int,
        n: Int,
        cache: Array<LongArray> = Array(bank.size) { LongArray(n + 1) { -1L } }
    ): Long {
        if (n == 0) {
            return 0
        }

        val cached = cache[i][n]
        if (cached >= 0) {
            return cached
        }

        var max = 0L
        for (j in i until bank.size - n + 1) {
            var value = bank[j].toLong()
            repeat(n - 1) {
                value *= 10
            }

            value += largestJoltage(bank, j + 1, n - 1, cache)
            if (value > max) {
                max = value
            }
        }

        cache[i][n] = max
        return max
    }

    override fun solveLevel1(): Any {
        return banks.sumOf {
            largestJoltage(it, 0, 2)
        }
    }

    override fun solveLevel2(): Any {
        return banks.sumOf {
            largestJoltage(it, 0, 12)
        }
    }
}
