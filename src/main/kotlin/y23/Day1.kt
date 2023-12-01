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


fun main() = solvePuzzle(year = 2023, day = 1) { Day1(it) }

class Day1(val input: Input) : Puzzle {
    data class Digit(val value: Int, val str: String)

    val digits = (1..9).map { Digit(it, it.toString()) } + listOf(
        Digit(1, "one"),
        Digit(2, "two"),
        Digit(3, "three"),
        Digit(4, "four"),
        Digit(5, "five"),
        Digit(6, "six"),
        Digit(7, "seven"),
        Digit(8, "eight"),
        Digit(9, "nine"),
    )

    override fun solveLevel1(): Any {
        return input.lines.sumOf { line ->
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }
            "$firstDigit$lastDigit".toInt()
        }
    }

    override fun solveLevel2(): Any {
        return input.lines.sumOf { line ->
            val firstDigit = digits.minBy { digit -> line.indexOf(digit.str).takeIf { it >= 0 } ?: Int.MAX_VALUE }
                ?: error("No first digit found")
            val lastDigit = digits.maxBy { digit -> line.lastIndexOf(digit.str).takeIf { it >= 0 } ?: Int.MIN_VALUE }
                ?: error("No last digit found")
            "${firstDigit.value}${lastDigit.value}".toInt()
        }
    }
}
