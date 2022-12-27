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


fun main() = solvePuzzle(year = 2022, day = 25) { Day25(it) }

typealias Snafu = String

fun Snafu.snafuToLong(): Long {
    var total = 0L
    forEachIndexed { i, c ->
        val offset = length - i - 1
        total += 5.0.pow(offset).toLong() * c.toSnafuDigit()
    }
    return total
}

fun Int.toSnafuChar() = when (this) {
    2 -> '2'
    1 -> '1'
    0 -> '0'
    -1 -> '-'
    -2 -> '='
    else -> throw IllegalArgumentException("Invalid int $this")
}

fun Char.toSnafuDigit() = when (this) {
    '2' -> 2
    '1' -> 1
    '0' -> 0
    '-' -> -1
    '=' -> -2
    else -> throw IllegalArgumentException("Invalid digit $this")
}

fun add(s1: Snafu, s2: Snafu): Snafu {
    var result = mutableListOf<Char>()
    var carry = 0
    var i = 0
    while (carry != 0 || i < maxOf(s1.length, s2.length)) {
        val c1 = s1.getOrNull(s1.length - 1 - i) ?: '0'
        val c2 = s2.getOrNull(s2.length - 1 - i) ?: '0'
        var sum = carry + c1.toSnafuDigit() + c2.toSnafuDigit()
        when {
            sum > 2 -> {
                carry = 1
                sum -= 5
            }
            sum < -2 -> {
                carry = -1
                sum += 5
            }
            else -> {
                carry = 0
            }
        }
        result += sum.toSnafuChar()
        i++
    }

    return String(result.reversed().toCharArray())
}

class Day25(val input: Input) : Puzzle {
    override fun solveLevel1(): Any {
        return input.lines.fold("0", ::add)
    }

    override fun solveLevel2(): Any {
        TODO()
    }
}
